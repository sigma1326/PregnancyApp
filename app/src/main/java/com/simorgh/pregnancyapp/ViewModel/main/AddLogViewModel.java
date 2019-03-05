package com.simorgh.pregnancyapp.ViewModel.main;

import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.database.model.Alcohol;
import com.simorgh.database.model.BloodPressure;
import com.simorgh.database.model.Cigarette;
import com.simorgh.database.model.Drug;
import com.simorgh.database.model.ExerciseTime;
import com.simorgh.database.model.Fever;
import com.simorgh.database.model.SleepTime;
import com.simorgh.database.model.Weight;
import com.simorgh.logger.Logger;
import com.simorgh.threadutils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddLogViewModel extends ViewModel {
    private Repository repository;
    private boolean isEditMode = false;
    private boolean saving = false;
    private MutableLiveData<Date> date = new MutableLiveData<>();
    private MutableLiveData<List<Drug>> drugs = new MutableLiveData<>();
    private MutableLiveData<Drug> drug = new MutableLiveData<>();
    private MutableLiveData<BloodPressure> bloodPressure = new MutableLiveData<>();
    private MutableLiveData<Weight> motherWeight = new MutableLiveData<>();
    private MutableLiveData<Fever> fever = new MutableLiveData<>();
    private MutableLiveData<Cigarette> cigarette = new MutableLiveData<>();
    private MutableLiveData<Alcohol> alcohol = new MutableLiveData<>();
    private MutableLiveData<SleepTime> sleepTime = new MutableLiveData<>();
    private MutableLiveData<ExerciseTime> exerciseTime = new MutableLiveData<>();
    private MutableLiveData<Boolean> editing = new MutableLiveData<>(true);
    private int editingPosition = -1;
    private final List<Drug> deleteList = new ArrayList<>();
    private Date inputDate;
    private boolean cleared = false;

    public AddLogViewModel() {
        ThreadUtils.execute(() -> {
            List<Drug> drugs = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Drug drug = new Drug();
                drug.setId(i);
                drug.setDrugName("دارو " + i);
                if (i % 2 == 0) {
                    drug.setInfo("توضیحات");
                } else {
                    drug.setInfo("");
                }
                drugs.add(drug);
            }
            ThreadUtils.runOnUIThread(() -> {
//                this.drugs.setValue(drugs);
            });
        });
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    public MutableLiveData<Date> getDate() {
        return date;
    }

    public void setDate(Date date) {
        ThreadUtils.runOnUIThread(() -> {
            this.date.setValue(date);
            Logger.i(date.toString());
        });
        loadData(date);
    }

    private void loadData(Date date) {
        deleteList.clear();
        cleared = false;
        ThreadUtils.runOnUIThread(() -> {
            drugs.setValue(null);
            drug.setValue(null);
            bloodPressure.setValue(null);
            motherWeight.setValue(null);
            fever.setValue(null);
            cigarette.setValue(null);
            alcohol.setValue(null);
            sleepTime.setValue(null);
            exerciseTime.setValue(null);
        });
        if (repository != null) {
            ThreadUtils.execute(() -> {
                List<Drug> drugs = repository.getDrugs(date);
                Fever fever = repository.getFever(date);
                BloodPressure bloodPressure = repository.getBloodPressure(date);
                Weight weight = repository.getWeight(date);
                Cigarette cigarette = repository.getCigarette(date);
                Alcohol alcohol = repository.getAlcohol(date);
                SleepTime sleepTime = repository.getSleepTime(date);
                ExerciseTime exerciseTime = repository.getExerciseTime(date);

                ThreadUtils.runOnUIThread(() -> {
                    this.drugs.setValue(drugs);
                    this.fever.setValue(fever);
                    this.bloodPressure.setValue(bloodPressure);
                    this.motherWeight.setValue(weight);
                    this.cigarette.setValue(cigarette);
                    this.alcohol.setValue(alcohol);
                    this.sleepTime.setValue(sleepTime);
                    this.exerciseTime.setValue(exerciseTime);
                });

                boolean edit = fever != null || bloodPressure != null || weight != null || cigarette != null || alcohol != null || sleepTime != null
                        || exerciseTime != null || (drugs != null && !drugs.isEmpty());
                setEditing(!edit || date.isToday());
            });
        }
    }



    public void removeDrug(long itemId) {
        List<Drug> temp = new ArrayList<>(Objects.requireNonNull(drugs.getValue()));
        for (int i = 0; i < Objects.requireNonNull(drugs.getValue()).size(); i++) {
            if (Objects.requireNonNull(temp).get(i).getId() == itemId && editingPosition != -1 && editingPosition == i) {
                editingPosition = -1;
                deleteList.add(Objects.requireNonNull(temp).get(i));
                temp.remove(i);
                break;
            }
        }
        if (Objects.requireNonNull(temp).size() != drugs.getValue().size()) {
            drugs.setValue(temp);
        }
    }

    public void saveLog(@NonNull Repository repository) {
        ThreadUtils.execute(() -> {
            List<Drug> drugList = drugs.getValue();
            if (drugList != null && !drugList.isEmpty()) {
                if (drugList.get(0).getDate() == null) {
                    for (int i = 0; i < drugList.size(); i++) {
                        Drug d = drugList.get(i);
                        d.setDate(date.getValue());
                        try {
                            String ids = i + "" + d.getDate();
                            long id = Long.parseLong(ids);
                            d.setId(id);
                        } catch (NumberFormatException e) {
                            Logger.printStackTrace(e);
                        }
                    }
                }
                if (cleared) {
                    repository.removeDrugs(date.getValue());
                }
                repository.insertDrugs(drugList);
            } else {
                if (cleared) {
                    repository.removeDrugs(date.getValue());
                }
            }

            BloodPressure b = bloodPressure.getValue();
            if (b != null && b.evaluate()) {
                if (b.getDate() == null) {
                    b.setDate(date.getValue());
                }
                repository.insertBloodPressure(b);
            } else if (cleared) {
                repository.removeBloodPressure(date.getValue());
            }

            Weight weight = motherWeight.getValue();
            if (weight != null && weight.evaluate()) {
                if (weight.getDate() == null) {
                    weight.setDate(date.getValue());
                }
                repository.insertWeight(weight);
            } else if (cleared) {
                repository.removeWeight(date.getValue());
            }

            Fever f = fever.getValue();
            if (f != null && f.evaluate()) {
                if (f.getDate() == null) {
                    f.setDate(date.getValue());
                }
                if (f.hasData()) {
                    repository.insertFever(f);
                } else {
                    repository.removeFever(f);
                }
            } else if (cleared) {
                repository.removeFever(date.getValue());
            }


            Cigarette c = cigarette.getValue();
            if (c != null && c.evaluate()) {
                if (c.getDate() == null) {
                    c.setDate(date.getValue());
                }
                if (c.hasData()) {
                    repository.insertCigarette(c);
                } else {
                    repository.removeCigarette(c);
                }
            }  else if (cleared) {
                repository.removeCigarette(date.getValue());
            }

            Alcohol al = alcohol.getValue();
            if (al != null && al.evaluate()) {
                if (al.getDate() == null) {
                    al.setDate(date.getValue());
                }
                if (al.hasData()) {
                    repository.insertAlcohol(al);
                } else {
                    repository.removeAlcohol(al);
                }
            } else if (cleared) {
                repository.removeAlcohol(date.getValue());
            }

            SleepTime st = sleepTime.getValue();
            if (st != null && st.evaluate()) {
                if (st.getDate() == null) {
                    st.setDate(date.getValue());
                }
                repository.insertSleepTime(st);
            } else if (cleared) {
                repository.removeSleepTime(date.getValue());
            }


            ExerciseTime et = exerciseTime.getValue();
            if (et != null && et.evaluate()) {
                if (et.getDate() == null) {
                    et.setDate(date.getValue());
                }
                repository.insertExerciseTime(et);
            } else if (cleared) {
                repository.removeExerciseTime(date.getValue());
            }

            if (!deleteList.isEmpty()) {
                repository.removeDrugs(deleteList);
            }

            if (!cleared) {
                ThreadUtils.runOnUIThread(() -> {
                    loadData(date.getValue());
                }, 200);
            } else {
                cleared = false;
            }
        });
    }

    public void addDrug(@NonNull Drug drug, itemAddedListener itemAddedListener) {
        ThreadUtils.execute(() -> {
            drug.setDate(date.getValue());
            List<Drug> temp = drugs.getValue();
            if (temp == null || Objects.requireNonNull(temp).isEmpty()) {
                temp = new ArrayList<>();
                temp.add(drug);
                List<Drug> finalTemp2 = temp;
                ThreadUtils.runOnUIThread(() -> {
                    drugs.setValue(finalTemp2);
                    itemAddedListener.itemAdded(true);
                });
            } else {
                boolean contains = false;
//                boolean modified = false;
                for (int i = 0; i < temp.size(); i++) {
                    if (temp.get(i).getId() == drug.getId()) {
                        if (editingPosition != -1) {
                            if (i == editingPosition) {
                                contains = true;
                                editingPosition = -1;
                                temp.get(i).setDrugName(drug.getDrugName());
                                temp.get(i).setInfo(drug.getInfo());
                                break;
                            }
                        }
                    }
                }
                if (!contains) {
                    Objects.requireNonNull(temp).add(drug);
                    List<Drug> finalTemp = temp;
                    ThreadUtils.runOnUIThread(() -> {
                        drugs.setValue(finalTemp);
                        itemAddedListener.itemAdded(true);
                    });
                } else {
                    List<Drug> finalTemp = temp;
                    ThreadUtils.runOnUIThread(() -> {
                        drugs.setValue(finalTemp);
                        itemAddedListener.itemAdded(true);
                    });
                }
            }
        });
    }

    public String checkErrors() {
        if (Objects.requireNonNull(bloodPressure.getValue()).evaluate()) {
            if (Objects.requireNonNull(bloodPressure.getValue()).getMaxPressure() < 7 || Objects.requireNonNull(bloodPressure.getValue()).getMaxPressure() > 19) {
                return "فشار خون صحیح نیست";
            }
            if (Objects.requireNonNull(bloodPressure.getValue()).getMinPressure() > bloodPressure.getValue().getMaxPressure()) {
                return "فشار خون صحیح نیست";
            }
        }

        if (Objects.requireNonNull(motherWeight.getValue()).evaluate()) {
            if (Objects.requireNonNull(motherWeight.getValue()).getWeight() <= 0) {
                return "وزن مادر صحیح نیست";
            }
        }

        if (Objects.requireNonNull(sleepTime.getValue()).evaluate()) {
            if (Objects.requireNonNull(sleepTime.getValue()).getHour() <= 0) {
                return "میزان خواب صحیح نیست";
            }
        }

        if (Objects.requireNonNull(exerciseTime.getValue()).evaluate()) {
            if (Objects.requireNonNull(exerciseTime.getValue()).getMinute() <= 0) {
                return "میزان زمان ورزش صحیح نیست";
            }
        }

        return null;
    }

    public void clearFields() {
        deleteList.clear();
        cleared = true;
        ThreadUtils.runOnUIThread(() -> {
            drugs.setValue(null);
            drug.setValue(null);
            bloodPressure.setValue(null);
            motherWeight.setValue(null);
            fever.setValue(null);
            cigarette.setValue(null);
            alcohol.setValue(null);
            sleepTime.setValue(null);
            exerciseTime.setValue(null);
        });
    }

    public void setEditingPosition(int position) {
        editingPosition = position;
    }

    public int getEditingPosition() {
        return editingPosition;
    }

    public void setEditing(boolean b) {
        ThreadUtils.runOnUIThread(() -> {
            editing.setValue(b);
        });
    }

    public MutableLiveData<Boolean> getEditing() {
        return editing;
    }

    public synchronized boolean isSaving() {
        return saving;
    }

    public synchronized void setSaving(boolean saving) {
        this.saving = saving;
    }

    public MutableLiveData<List<Drug>> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs.setValue(drugs);
    }

    public MutableLiveData<Drug> getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug.setValue(drug);
    }

    public MutableLiveData<BloodPressure> getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(BloodPressure bloodPressure) {
        this.bloodPressure.setValue(bloodPressure);
    }

    public MutableLiveData<Weight> getMotherWeight() {
        return motherWeight;
    }

    public void setMotherWeight(Weight motherWeight) {
        this.motherWeight.setValue(motherWeight);
    }

    public MutableLiveData<Fever> getFever() {
        return fever;
    }

    public void setFever(Fever fever) {
        this.fever.setValue(fever);
    }

    public MutableLiveData<Cigarette> getCigarette() {
        return cigarette;
    }

    public void setCigarette(Cigarette cigarette) {
        this.cigarette.setValue(cigarette);
    }

    public MutableLiveData<Alcohol> getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(Alcohol alcohol) {
        this.alcohol.setValue(alcohol);
    }

    public MutableLiveData<SleepTime> getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(SleepTime sleepTime) {
        this.sleepTime.setValue(sleepTime);
    }

    public MutableLiveData<ExerciseTime> getExerciseTime() {
        return exerciseTime;
    }

    public void setExerciseTime(ExerciseTime exerciseTime) {
        this.exerciseTime.setValue(exerciseTime);
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public interface itemAddedListener {
        public void itemAdded(boolean success);
    }
}
