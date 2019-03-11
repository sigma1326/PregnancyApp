package com.simorgh.pregnancyapp.ViewModel.main;

import android.util.Pair;

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
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class AddLogViewModel extends ViewModel {
    private Repository repository;
    private boolean isEditMode = false;
    private boolean saving = false;
    private final MutableLiveData<Date> date = new MutableLiveData<>();
    private final MutableLiveData<List<Drug>> drugs = new MutableLiveData<>();
    private final MutableLiveData<Drug> drug = new MutableLiveData<>();
    private final MutableLiveData<BloodPressure> bloodPressure = new MutableLiveData<>();
    private final MutableLiveData<Weight> motherWeight = new MutableLiveData<>();
    private final MutableLiveData<Fever> fever = new MutableLiveData<>();
    private final MutableLiveData<Cigarette> cigarette = new MutableLiveData<>();
    private final MutableLiveData<Alcohol> alcohol = new MutableLiveData<>();
    private final MutableLiveData<SleepTime> sleepTime = new MutableLiveData<>();
    private final MutableLiveData<ExerciseTime> exerciseTime = new MutableLiveData<>();
    private final MutableLiveData<Boolean> editing = new MutableLiveData<>(true);
    private int editingPosition = -1;
    private final List<Drug> deleteList = new ArrayList<>();
    private Date inputDate;
    private boolean cleared = false;
    private final CompositeDisposable disposable = new CompositeDisposable();


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

    public void setDate(Date value) {
        ThreadUtils.onUI(() -> date.setValue(value));
        loadData(value);
    }

    private void loadData(Date value) {
        clearFields(false);

        if (repository != null) {
            Disposable d = Observable.create(emitter -> {
                List<Drug> drugs = repository.getDrugs(value);
                Fever fever = repository.getFever(value);
                BloodPressure bloodPressure = repository.getBloodPressure(value);
                Weight weight = repository.getWeight(value);
                Cigarette cigarette = repository.getCigarette(value);
                Alcohol alcohol = repository.getAlcohol(value);
                SleepTime sleepTime = repository.getSleepTime(value);
                ExerciseTime exerciseTime = repository.getExerciseTime(value);

                if (drugs != null) {
                    emitter.onNext(drugs);
                }
                if (fever != null) {
                    emitter.onNext(fever);
                }
                if (bloodPressure != null) {
                    emitter.onNext(bloodPressure);
                }
                if (weight != null) {
                    emitter.onNext(weight);
                }
                if (cigarette != null) {
                    emitter.onNext(cigarette);
                }
                if (alcohol != null) {
                    emitter.onNext(alcohol);
                }
                if (sleepTime != null) {
                    emitter.onNext(sleepTime);
                }
                if (exerciseTime != null) {
                    emitter.onNext(exerciseTime);
                }

                boolean edit = fever != null || bloodPressure != null || weight != null || cigarette != null || alcohol != null || sleepTime != null
                        || exerciseTime != null || (drugs != null && !drugs.isEmpty());

                emitter.onNext(edit);

                emitter.onComplete();
            }).compose(Repository.apply())
                    .subscribe(object -> {
                        if (object instanceof List) {
                            drugs.setValue(((List<Drug>) object));
                        } else if (object instanceof Fever) {
                            fever.setValue(((Fever) object));
                        } else if (object instanceof BloodPressure) {
                            bloodPressure.setValue(((BloodPressure) object));
                        } else if (object instanceof Weight) {
                            motherWeight.setValue(((Weight) object));
                        } else if (object instanceof Cigarette) {
                            cigarette.setValue(((Cigarette) object));
                        } else if (object instanceof Alcohol) {
                            alcohol.setValue(((Alcohol) object));
                        } else if (object instanceof SleepTime) {
                            sleepTime.setValue(((SleepTime) object));
                        } else if (object instanceof ExerciseTime) {
                            exerciseTime.setValue(((ExerciseTime) object));
                        } else if (object instanceof Boolean) {
                            setEditing(!((Boolean) object) || value.isToday());
                        }
                    }, Logger::printStackTrace);
            disposable.add(d);
        }
    }


    public void removeDrug(long itemId) {
        Disposable d = Observable.fromCallable(() -> {
            List<Drug> temp = new ArrayList<>(Objects.requireNonNull(drugs.getValue()));
            for (int i = 0; i < Objects.requireNonNull(drugs.getValue()).size(); i++) {
                if (Objects.requireNonNull(temp).get(i).getId() == itemId && editingPosition != -1 && editingPosition == i) {
                    editingPosition = -1;
                    deleteList.add(Objects.requireNonNull(temp).get(i));
                    temp.remove(i);
                    break;
                }
            }
            return temp;
        }).compose(Repository.apply())
                .filter(drugs1 -> drugs.getValue() != null)
                .filter(drugs1 -> drugs1.size() != drugs.getValue().size())
                .subscribe(drugs::setValue, Logger::printStackTrace);
        disposable.add(d);
    }

    public void saveLog(@NonNull Repository repository) {
        Disposable dis = Completable.fromCallable(() -> {
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
            } else if (cleared) {
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
                ThreadUtils.onUI(() -> {
                    loadData(date.getValue());
                }, 200);
            } else {
                cleared = false;
            }
            return true;
        }).compose(Repository.applyIOCompletable())
                .subscribe();
        disposable.add(dis);
    }

    public void addDrug(@NonNull Drug drug, itemAddedListener itemAddedListener) {
        Disposable d = Observable.just(date.getValue())
                .filter(date1 -> date1 != null)
                .zipWith(Observable.just(drug), (date1, drug1) -> {
                    drug1.setDate(date1);
                    return drug1;
                }).map(drug1 -> {
                    boolean createArray = drugs.getValue() == null || drugs.getValue().isEmpty();
                    return new Pair<Drug, Boolean>(drug1, createArray);
                })
                .flatMap(drugBooleanPair -> {
                    if (drugBooleanPair.second) {
                        return Observable.fromCallable(() -> {
                            List<Drug> temp = new ArrayList<>();
                            temp.add(drugBooleanPair.first);
                            return temp;
                        });
                    } else {
                        return Observable.fromCallable(() -> {
                            boolean contains = false;
                            List<Drug> temp = drugs.getValue();
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
                            if (contains) {
                                return temp;
                            } else {
                                temp.add(drugBooleanPair.first);
                                return temp;
                            }
                        });
                    }
                })
                .compose(Repository.apply())
                .subscribe(drugs1 -> {
                    drugs.setValue(drugs1);
                    itemAddedListener.itemAdded(true);
                }, Logger::printStackTrace);

        disposable.add(d);
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

    public void clearFields(boolean flagClear) {
        deleteList.clear();
        cleared = flagClear;
        ThreadUtils.onUI(() -> {
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
        ThreadUtils.onUI(() -> {
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


    @Override
    protected void onCleared() {
        if (!disposable.isDisposed()) {
            disposable.clear();
        }
        super.onCleared();
    }

    public interface itemAddedListener {
        public void itemAdded(boolean success);
    }
}
