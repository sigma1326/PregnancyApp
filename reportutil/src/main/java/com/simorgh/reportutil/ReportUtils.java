package com.simorgh.reportutil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
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
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class ReportUtils {
    @SuppressLint("CheckResult")
    public synchronized static Observable<Boolean> createReport(@NonNull Activity activity, @NonNull Repository repository, ReportState reportState) {
        final RxPermissions rxPermissions = new RxPermissions((FragmentActivity) activity);
        return rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(granted -> buildTable(activity, reportState, repository));

    }

    @SuppressLint("DefaultLocale")
    private static Observable<Boolean> buildTable(@NonNull Activity activity, ReportState reportState, Repository repository) {
        return Observable.fromCallable(() -> {
            String path = Environment.getExternalStorageDirectory() + "/" + "pregnancy_report" + ".pdf";
            File pdfFile = new File(path);
            if (pdfFile.exists()) {
                pdfFile.delete();
            }

            Date start = repository.getUserOnly().getPregnancyStartDate();


            Document document = new Document(PageSize.A4);

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(path));

            // Open to write
            document.open();


            // Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Pregnancy App");
            document.addCreator("Pregnancy App");

            BaseColor mColorAccent = new BaseColor(0, 0, 0, 255);
            float mHeadingFontSize = 12.0f;
            BaseFont urName = BaseFont.createFont("assets/fonts/iransans_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));


            // Title Order Details...
            // Adding Title....
            Font titleFont = new Font(urName, 12, Font.NORMAL, BaseColor.BLACK);

            // Fields of Order Details...
            // Adding Chunks for Title and value
            Font f = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Font fSmall = new Font(urName, 10f, Font.NORMAL, mColorAccent);
            PdfPTable table = new PdfPTable(5);
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.setExtendLastRow(false);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);


            PdfPCell title = new PdfPCell(new Paragraph("گزارش", titleFont));
            title.setPadding(10);
            title.setHorizontalAlignment(Element.ALIGN_CENTER);
            title.setColspan(5);
            title.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(title);

            PdfPCell pregDayColumn = new PdfPCell(new Paragraph("روز بارداری", f));
            pregDayColumn.setPadding(10);
            pregDayColumn.setColspan(1);
            pregDayColumn.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            pregDayColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(pregDayColumn);

            PdfPCell dateColumn = new PdfPCell(new Paragraph("تاریخ", f));
            dateColumn.setPadding(10);
            dateColumn.setColspan(1);
            dateColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
            dateColumn.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(dateColumn);

            PdfPCell statusColumn = new PdfPCell(new Paragraph("حالت‌ها", f));
            statusColumn.setPadding(10);
            statusColumn.setColspan(3);
            statusColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
            statusColumn.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.addCell(statusColumn);


            List<Date> dateList = repository.getLoggedDatesList(reportState.startDate, reportState.endDate);

            boolean addColumn;
            Phrase phrase;
            PdfPTable tableSmall;
            PersianCalendar persianCalendar;

            for (Date d : dateList) {
                addColumn = false;

                tableSmall = new PdfPTable(3);
                tableSmall.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                tableSmall.setExtendLastRow(false);
                tableSmall.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cell;

                if (reportState.drugs) {
                    List<Drug> drugList = repository.getDrugs(d);
                    if (drugList != null && !drugList.isEmpty()) {
                        addColumn = true;

                        for (Drug drug : drugList) {
                            phrase = new Phrase("دارو", fSmall);
                            phrase.getChunks().get(0).setLineHeight(10);
                            phrase.setLeading(30, 0);
                            cell = new PdfPCell(phrase);
                            cell.setPadding(10);
                            cell.setColspan(1);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            tableSmall.addCell(cell);

                            phrase = new Phrase(drug.getDrugName(), fSmall);
                            phrase.getChunks().get(0).setLineHeight(10);
                            phrase.setLeading(30, 0);
                            cell = new PdfPCell(phrase);
                            cell.setPadding(10);
                            cell.setColspan(1);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            tableSmall.addCell(cell);


                            String info = drug.getInfo();
                            if (info == null || info.isEmpty()) {
                                info = "     ";
                            }
                            phrase = new Phrase(info, fSmall);
                            phrase.getChunks().get(0).setLineHeight(10);
                            phrase.setLeading(30, 0);
                            cell = new PdfPCell(phrase);
                            cell.setPadding(10);
                            cell.setColspan(1);
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            tableSmall.addCell(cell);
                        }
                    }
                }

                //blood pressure
                if (reportState.bloodPressure) {
                    BloodPressure bloodPressure = repository.getBloodPressure(d);
                    if (bloodPressure != null) {
                        addColumn = true;
                        phrase = new Phrase("فشار خون", fSmall);
                        phrase.getChunks().get(0).setLineHeight(15);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);

                        phrase = new Phrase(String.format("کمینه:%d بیشینه:%d", (int) bloodPressure.getMinPressure(), (int) bloodPressure.getMaxPressure()), fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);


                        String info = bloodPressure.getInfo();
                        if (info == null || info.isEmpty()) {
                            info = "     ";
                        }
                        phrase = new Phrase(info, fSmall);
                        phrase.getChunks().get(0).setLineHeight(15);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);
                    }
                }


                //Weight
                if (reportState.motherWeight) {
                    Weight weight = repository.getWeight(d);
                    if (weight != null) {
                        addColumn = true;
                        phrase = new Phrase("وزن مادر", fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);

                        phrase = new Phrase(String.format("%d کیلوگرم", (int) weight.getWeight()), fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);


                        String info = weight.getInfo();
                        if (info == null || info.isEmpty()) {
                            info = "     ";
                        }
                        phrase = new Phrase(info, fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);
                    }
                }


                //Fever
                if (reportState.fever) {
                    Fever fever = repository.getFever(d);
                    if (fever != null) {
                        addColumn = true;
                        phrase = new Phrase("تب و لرز", fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);

                        phrase = new Phrase(fever.hasFever() ? "داشتم" : "نداشتم", fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);


                        String info = fever.getInfo();
                        if (info == null || info.isEmpty()) {
                            info = "     ";
                        }
                        phrase = new Phrase(info, fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);
                    }
                }


                //Cigarette
                if (reportState.cigarette) {
                    Cigarette cigarette = repository.getCigarette(d);
                    if (cigarette != null) {
                        addColumn = true;
                        phrase = new Phrase("مصرف سیگار", fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);

                        phrase = new Phrase(cigarette.isUseCigarette() ? "داشتم" : "نداشتم", fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);


                        String info = cigarette.getInfo();
                        if (info == null || info.isEmpty()) {
                            info = "     ";
                        }
                        phrase = new Phrase(info, fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);
                    }
                }

                //Alcohol
                if (reportState.alcohol) {
                    Alcohol alcohol = repository.getAlcohol(d);
                    if (alcohol != null) {
                        addColumn = true;
                        phrase = new Phrase("مصرف الکل", fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);

                        phrase = new Phrase(alcohol.isUseAlcohol() ? "داشتم" : "نداشتم", fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);


                        String info = alcohol.getInfo();
                        if (info == null || info.isEmpty()) {
                            info = "     ";
                        }
                        phrase = new Phrase(info, fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);
                    }
                }

                //SleepTime
                if (reportState.sleepTime) {
                    SleepTime sleepTime = repository.getSleepTime(d);
                    if (sleepTime != null) {
                        addColumn = true;
                        phrase = new Phrase("میزان خواب", fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);

                        phrase = new Phrase(String.format("%d ساعت", (int) sleepTime.getHour()), fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);


                        String info = sleepTime.getInfo();
                        if (info == null || info.isEmpty()) {
                            info = "     ";
                        }
                        phrase = new Phrase(info, fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);
                    }
                }


                //ExerciseTime
                if (reportState.exerciseTime) {
                    ExerciseTime exerciseTime = repository.getExerciseTime(d);
                    if (exerciseTime != null) {
                        addColumn = true;
                        phrase = new Phrase("مدت زمان ورزش", fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);

                        phrase = new Phrase(String.format("%d دقیقه", (int) exerciseTime.getMinute()), fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);


                        String info = exerciseTime.getInfo();
                        if (info == null || info.isEmpty()) {
                            info = "     ";
                        }
                        phrase = new Phrase(info, fSmall);
                        phrase.getChunks().get(0).setLineHeight(10);
                        phrase.setLeading(30, 0);
                        cell = new PdfPCell(phrase);
                        cell.setPadding(10);
                        cell.setColspan(1);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        tableSmall.addCell(cell);
                    }
                }


                if (addColumn) {
                    persianCalendar = CalendarTool.GregorianToPersian(d.getCalendar());

                    PdfPCell dayNumberCell;
                    int dayNum = (int) CalendarTool.getDaysFromDiff(d.getCalendar(), start.getCalendar()) + 1;
                    phrase = new Phrase(String.valueOf(dayNum), f);
                    phrase.getChunks().get(0).setLineHeight(20);
                    phrase.setLeading(30, 0);
                    dayNumberCell = new PdfPCell(phrase);
                    dayNumberCell.setPadding(10);
                    dayNumberCell.setColspan(1);
                    dayNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dayNumberCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


                    PdfPCell dateCell;
                    String date = String.format("%d/%d/%d", persianCalendar.getPersianYear(), persianCalendar.getPersianMonth() + 1, persianCalendar.getPersianDay());
                    phrase = new Phrase(date, f);
                    phrase.getChunks().get(0).setLineHeight(20);
                    phrase.setLeading(30, 0);
                    dateCell = new PdfPCell(phrase);
                    dateCell.setPadding(10);
                    dateCell.setColspan(1);
                    dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dateCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);


                    table.addCell(dayNumberCell);
                    table.addCell(dateCell);
                    PdfPCell pdfPCell = new PdfPCell(tableSmall);
                    pdfPCell.setColspan(3);
                    table.addCell(pdfPCell);
                }
            }


            document.add(table);
            document.close();
            return path;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(path -> openFile(activity, path));
    }

    private static Observable<Boolean> openFile(@NonNull Activity activity, String path) {
        return Observable.fromCallable(() -> {
            File file = new File(path);

            Intent openFile = new Intent(Intent.ACTION_VIEW);
            openFile.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);

            Uri apkURI = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", file);
            openFile.setDataAndType(apkURI, "application/pdf");
            openFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(openFile);
            return true;
        }).subscribeOn(AndroidSchedulers.mainThread());
    }
}
