// GUI Application and Database for a University Management System
// Cooper Ott, Ryan Curran, Ethan Fannon
package com.mycompany.database;

// database imports
import java.sql.*;
import java.util.Scanner;
import oracle.jdbc.pool.*;
import oracle.jdbc.*;
import java.util.*;

// Make all necessary imports for JavaFX 
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    public static OracleDataSource oDS;
    public static Connection jsqlConn;
    public static PreparedStatement jsqlStmt;
    public static ResultSet jsqlResults;
    // Declare static ArrayLists to store semesters, students, faculty, courses...
    private static ArrayList<Semester> semesters;
    private static ArrayList<Student> students;
    private static ArrayList<Faculty> faculty;
    private static ArrayList<Course> courses;
    private static ArrayList<Schedule> schedules;
    private static ArrayList<Enrollment> enrollments;

    // Generates a Report of Courses in a Semester
    private static void coursesInSemester() {
        // Create a new stage for report generation
        Stage stage = new Stage();

        stage.setTitle("JMakers | Generate Report"); // Sets Box Title

        ComboBox<Semester> cmboSemesters = new ComboBox<Semester>(); // Creates a dropdown pulling from Semester Class
        cmboSemesters.setStyle("-fx-font-family: monospace"); // Set font style

        TextArea textArea = new TextArea(); // set text area

        // Populate the dropdown with available semesters
        for (Semester sem : semesters) {
            cmboSemesters.getItems().add(sem); // Adds the array of semesters
        }

        // Defines a variable to hold selected semester
        var semester = new Object() {
            Semester sem = null;
        };
        // Defines an action when a semester is selected from dropdown
        cmboSemesters.setOnAction(f -> {
            semester.sem = cmboSemesters.getSelectionModel().getSelectedItem();
            textArea.clear();
            textArea.appendText("Generating Report...\n");
            for (Course c : courses) { // Display courses for the selected semester in the text area
                if (c.getSemester() == null) {
                    System.out.println("Cry about it");
                }
                if (c.getSemester().equals(semester.sem)) {
                    textArea.appendText(c.toString() + "\n");
                }
            }
            textArea.appendText("Done.\n");
        });

        // Create a grid layout for the stage
        GridPane grid = new GridPane();
        // Adds a dropdown menu and text area to grid layouyt
        grid.add(cmboSemesters, 0, 0);
        grid.add(textArea, 0, 1);

        grid.setAlignment(Pos.CENTER);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene); // Set a scene to the stage and display stage
        stage.show();
    }
    // Generation Report for Courses Taught by a Faculty Member in a Semester

    private static void coursesByFaculty() {

        Stage stage = new Stage();

        stage.setTitle("JMakers | Generate Report");

        ComboBox<Semester> cmboSemesters = new ComboBox<Semester>();
        cmboSemesters.setStyle("-fx-font-family: monospace");

        for (Semester sem : semesters) {
            cmboSemesters.getItems().add(sem);
        }

        ComboBox<Faculty> cmboFaculty = new ComboBox<Faculty>();
        cmboFaculty.setStyle("-fx-font-family: monospace");

        for (Faculty fac : faculty) {
            cmboFaculty.getItems().add(fac);
        }

        TextArea textArea = new TextArea();

        var selection = new Object() {
            Semester sem = null;
            Faculty fac = null;
        };

        cmboSemesters.setOnAction(f -> {
            // get the selected semester
            selection.sem = cmboSemesters.getSelectionModel().getSelectedItem();
            // Clear the text area
            textArea.clear();
            textArea.appendText("Generating Report...\n");
            if (selection.sem != null && selection.fac != null) { // if semester and faculty selections are null
                for (Schedule s : schedules) { // Displays courses for selected semester and faculty in text area
                    if (s.getFaculty().equals(selection.fac) && s.getCourse().getSemester().equals(selection.sem)) {
                        textArea.appendText(s.getCourse().toString() + "\n");
                    }
                }
            }
            textArea.appendText("Done.\n");
        });

        cmboFaculty.setOnAction(f -> {
            selection.fac = cmboFaculty.getSelectionModel().getSelectedItem();
            textArea.clear();
            textArea.appendText("Generating Report...\n");
            if (selection.sem != null && selection.fac != null) {
                for (Schedule s : schedules) {
                    if (s.getFaculty().equals(selection.fac) && s.getCourse().getSemester().equals(selection.sem)) {
                        textArea.appendText(s.getCourse().toString() + "\n");
                    }
                }
            }
            textArea.appendText("Done.\n");
        });

        GridPane grid = new GridPane();

        grid.add(cmboSemesters, 0, 0);
        grid.add(cmboFaculty, 1, 0);
        grid.add(textArea, 0, 1, 2, 1);

        grid.setAlignment(Pos.CENTER);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
    // Generation Report for Courses a Student is enrolled in for a Single Semester

    private static void coursesOfStudent() {
        Stage stage = new Stage();

        stage.setTitle("JMakers | Generate Report");

        ComboBox<Semester> cmboSemesters = new ComboBox<Semester>();
        cmboSemesters.setStyle("-fx-font-family: monospace");

        for (Semester sem : semesters) {
            cmboSemesters.getItems().add(sem);
        }

        ComboBox<Student> cmboStudent = new ComboBox<Student>();
        cmboStudent.setStyle("-fx-font-family: monospace");

        for (Student stu : students) {
            cmboStudent.getItems().add(stu);
        }

        TextArea textArea = new TextArea();

        var selection = new Object() {
            Semester sem = null;
            Student stu = null;
        };

        cmboSemesters.setOnAction(f -> {
            selection.sem = cmboSemesters.getSelectionModel().getSelectedItem();
            textArea.clear();
            textArea.appendText("Generating Report...\n");
            if (selection.sem != null && selection.stu != null) {
                for (Enrollment e : enrollments) {
                    if (e.getStudent().equals(selection.stu) && e.getCourse().getSemester().equals(selection.sem)) {
                        textArea.appendText(e.getCourse().toString() + "\n");
                    }
                }
            }
            textArea.appendText("Done.\n");
        });

        cmboStudent.setOnAction(f -> {
            selection.stu = cmboStudent.getSelectionModel().getSelectedItem();
            textArea.clear();
            textArea.appendText("Generating Report...\n");
            if (selection.sem != null && selection.stu != null) {
                for (Enrollment e : enrollments) {
                    if (e.getStudent().equals(selection.stu) && e.getCourse().getSemester().equals(selection.sem)) {
                        textArea.appendText(e.getCourse().toString() + "\n");
                    }
                }
            }
            textArea.appendText("Done.\n");
        });

        GridPane grid = new GridPane();

        grid.add(cmboSemesters, 0, 0);
        grid.add(cmboStudent, 1, 0);
        grid.add(textArea, 0, 1, 2, 1);

        grid.setAlignment(Pos.CENTER);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
    // Generation Report for Students Enrolled in a Single Course in a Certain
    // Semester

    private static void studentsInCourse() {
        Stage stage = new Stage();

        stage.setTitle("JMakers | Generate Report");

        ComboBox<Course> cmboCourses = new ComboBox<Course>();
        cmboCourses.setStyle("-fx-font-family: monospace");

        TextArea textArea = new TextArea();

        for (Course crs : courses) {
            cmboCourses.getItems().add(crs);
        }

        var select = new Object() {
            Course crs = null;
        };
        cmboCourses.setOnAction(f -> {
            select.crs = cmboCourses.getSelectionModel().getSelectedItem();
            textArea.clear();
            textArea.appendText("Generating Report...\n");
            for (Enrollment e : enrollments) {
                if (e.getCourse().equals(select.crs)) {
                    textArea.appendText(e.getStudent().toString() + "\n");
                }
            }
            textArea.appendText("Done.\n");
        });

        GridPane grid = new GridPane();

        grid.add(cmboCourses, 0, 0);
        grid.add(textArea, 0, 1);

        grid.setAlignment(Pos.CENTER);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    // Main Application Page
    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
        stage.setTitle("JMakers | Main Menu");

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        var scene = new Scene(pane, 640, 480); // Sets the size of the grid for Main App

        Label createLabel = new Label("Create..."); // Create Text
        createLabel.setMinWidth(300);
        pane.add(createLabel, 0, 0);

        Button createSemester = new Button("Semester"); // Semester Button
        createSemester.setMinWidth(300);
        createSemester.setOnAction(e -> {
            Semester.create(semesters);
        });
        pane.add(createSemester, 0, 1);

        Button createFaculty = new Button("Faculty"); // Create Faculty Button
        createFaculty.setMinWidth(300);
        createFaculty.setOnAction(e -> {
            Faculty.create(faculty);
        });
        pane.add(createFaculty, 0, 2);

        Button createStudent = new Button("Student"); // Create Student Button
        createStudent.setMinWidth(300);
        createStudent.setOnAction(e -> {
            Student.create(students);
        });
        pane.add(createStudent, 0, 3);

        Button createCourse = new Button("Course"); // Create Course Button
        createCourse.setMinWidth(300);
        createCourse.setOnAction(e -> {
            Course.create(courses, semesters);
        });
        pane.add(createCourse, 0, 4);

        Label editLabel = new Label("Edit..."); // Edit text Label
        editLabel.setMinWidth(300);
        pane.add(editLabel, 1, 0);

        Button editSemester = new Button("Semester"); // Edit Semester Button
        editSemester.setMinWidth(300);
        editSemester.setOnAction(e -> {
            Semester.edit(semesters);
        });
        pane.add(editSemester, 1, 1);

        Button editFaculty = new Button("Faculty"); // Edit Faculty Button
        editFaculty.setMinWidth(300);
        editFaculty.setOnAction(e -> {
            Faculty.edit(faculty);
        });
        pane.add(editFaculty, 1, 2);

        Button editStudent = new Button("Student"); // Edit Student Button
        editStudent.setMinWidth(300);
        editStudent.setOnAction(e -> {
            Student.edit(students);
        });
        pane.add(editStudent, 1, 3);

        Button editCourse = new Button("Course"); // Edit Course Button
        editCourse.setMinWidth(300);
        editCourse.setOnAction(e -> {
            Course.edit(courses, semesters);
        });
        pane.add(editCourse, 1, 4);

        Label lblAssign = new Label("Assign..."); // Assign Text Label
        lblAssign.setMinWidth(600);
        pane.add(lblAssign, 0, 5, 2, 1);

        Button btnStuToCourse = new Button("Student to Course"); // Student to Course Button
        btnStuToCourse.setMinWidth(600);
        btnStuToCourse.setOnAction(e -> {
            Enrollment.assign(students, courses, enrollments);
        });
        pane.add(btnStuToCourse, 0, 6, 2, 1);

        Button btnFacultyToCourse = new Button("Faculty to Course"); // Faculty to Course Button
        btnFacultyToCourse.setMinWidth(600);
        btnFacultyToCourse.setOnAction(e -> {
            Schedule.assign(faculty, courses, schedules);
        });
        pane.add(btnFacultyToCourse, 0, 7, 2, 1);

        Label lblReport = new Label("Generate Report");
        lblReport.setMinWidth(600);
        pane.add(lblReport, 0, 8, 2, 1);

        Button btnCIS = new Button("Courses Taught In <Semester>");
        btnCIS.setMinWidth(300);
        btnCIS.setOnAction(e -> {
            coursesInSemester();
        });
        pane.add(btnCIS, 0, 9);

        Button btnCTF = new Button("Courses Taught by <Faculty>");
        btnCTF.setMinWidth(300);
        btnCTF.setOnAction(e -> {
            coursesByFaculty();
        });
        pane.add(btnCTF, 1, 9);

        Button btnCSS = new Button("Courses Enrolled by <Student>");
        btnCSS.setMinWidth(300);
        btnCSS.setOnAction(e -> {
            coursesOfStudent();
        });
        pane.add(btnCSS, 0, 10);

        Button btnCAA = new Button("Students Enrolled in <Course>");
        btnCAA.setMinWidth(300);
        btnCAA.setOnAction(e -> {
            studentsInCourse();
        });
        pane.add(btnCAA, 1, 10);

        stage.setScene(scene);
        stage.show(); // Shows the Entire App
    }

    public static void main(String[] args) {
        // Initializes lists to store semesters, students, faculty...
        semesters = new ArrayList<Semester>();
        students = new ArrayList<Student>();
        faculty = new ArrayList<Faculty>();
        courses = new ArrayList<Course>();
        schedules = new ArrayList<Schedule>();
        enrollments = new ArrayList<Enrollment>();
        try {
            runDBQuery("SELECT * FROM JAVAUSER.SEMESTER", 'r');
            while (jsqlResults.next()) {
                semesters.add(new Semester( // Overloaded constructor to use a specific ID
                        jsqlResults.getInt(1), // semesterID
                        jsqlResults.getString(2), // period
                        jsqlResults.getInt(3) // year
                ));
            }
            runDBQuery("SELECT * FROM JAVAUSER.FACULTY", 'r');
            while (jsqlResults.next()) {
                faculty.add(new Faculty( // Overloaded constructor to use a specific ID
                        jsqlResults.getInt(1), // facultyID
                        jsqlResults.getString(2), // name
                        jsqlResults.getString(3), // email
                        jsqlResults.getString(4), // buildingName
                        jsqlResults.getInt(5), // officeNumber
                        jsqlResults.getLong(6), // phoneNumber
                        jsqlResults.getString(7), // department
                        jsqlResults.getString(8) // position
                ));
            }
            runDBQuery("SELECT * FROM JAVAUSER.STUDENT", 'r');
            while (jsqlResults.next()) {
                students.add(new Student(jsqlResults.getInt(1), // studentID
                        jsqlResults.getString(2), // fullName
                        jsqlResults.getString(3), // Address
                        jsqlResults.getString(4), // email
                        jsqlResults.getDouble(5), // gpa
                        jsqlResults.getInt(6) // ssn
                ));
            }
            runDBQuery("SELECT * FROM JAVAUSER.COURSE", 'r');
            while (jsqlResults.next()) {

                int semesterID = jsqlResults.getInt(9);
                Semester foundSemester = null;
                for (Semester s : semesters) {
                    if (s.getID() == semesterID) {
                        foundSemester = s;
                    }
                }

                courses.add(new Course(jsqlResults.getInt(1), // courseID
                        jsqlResults.getString(2), // prefix
                        jsqlResults.getInt(3), // number
                        jsqlResults.getString(4), // name
                        jsqlResults.getString(5), // days
                        jsqlResults.getString(6), // startTime
                        jsqlResults.getString(7), // endTime
                        jsqlResults.getInt(8), // creditHours
                        foundSemester
                ));
            }
            runDBQuery("SELECT * FROM JAVAUSER.SCHEDULE", 'r');
            while (jsqlResults.next()) {
                // Create a Schedule object and add it to the schedules list
                int courseID = jsqlResults.getInt(2);
                int facultyID = jsqlResults.getInt(3);

                Course foundCourse = null;
                Faculty foundFaculty = null;

                for (Course c : courses) {
                    if (c.getID() == courseID) {
                        foundCourse = c;
                    }
                }

                for (Faculty f : faculty) {
                    if (f.getFacultyID() == facultyID) {
                        foundFaculty = f;
                    }
                }

                if (foundCourse == null) {
                    System.out.println("No course found!");
                } else if (foundFaculty == null) {
                    System.out.println("No student found!");
                } else {
                    schedules.add(new Schedule(foundCourse, foundFaculty));
                }
            }

            runDBQuery("SELECT * FROM JAVAUSER.ENROLLMENT", 'r');
            while (jsqlResults.next()) {
                // Create a Schedule object and add it to the schedules list
                int courseID = jsqlResults.getInt(2);
                int studentID = jsqlResults.getInt(3);

                Course foundCourse = null;
                Student foundStudent = null;

                for (Course c : courses) {
                    if (c.getID() == courseID) {
                        foundCourse = c;
                    }
                }

                for (Student s : students) {
                    if (s.getStuID() == studentID) {
                        foundStudent = s;
                    }
                }

                enrollments.add(new Enrollment(foundCourse, foundStudent));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println();
        System.out.println("");
        launch(); // Launches the Application
    }

    public void stop() {

        try {
            for (Semester s : semesters) {
                runDBQuery("Select semesterID From Semester Where semesterID = " + s.getID(), 'r');
                if (jsqlResults.next()) {
                    runDBQuery("UPDATE Semester SET periods = '" + s.getPeriod() + "', years = " + s.getYear() + " WHERE semesterID = " + s.getID(), 'u');
                } else {
                    runDBQuery( // run
                            "Insert into Semester (semesterID, periods, years) values ("
                            + // Set up command
                            s.getID() + ", '"
                            + // semesterID
                            s.getPeriod() + "', "
                            + // period
                            s.getYear()
                            + // year
                            ")", 'u');
                }
            }

            for (Faculty f : faculty) {
                runDBQuery("SELECT facultyID FROM Faculty WHERE facultyID = " + f.getFacultyID(), 'r');
                if (jsqlResults.next()) {
                    runDBQuery("UPDATE Faculty SET fullName = '" + f.getName() + "', email = '" + f.getEmail() + "', buildingName = '" + f.getBuilding() + "', officeNumber = " + f.getOffice() + ", phoneNumber = " + f.getPhone() + ", department = '" + f.getDepartment() + "', positions = '" + f.getPosition() + "' WHERE facultyID = " + f.getFacultyID(), 'u');
                } else {
                    runDBQuery( // run
                            "INSERT INTO Faculty (facultyID, fullName, email, buildingName, officeNumber, phoneNumber, department, positions) VALUES ("
                            + // Set up command
                            f.getFacultyID() + ", '"
                            + // facultyID
                            f.getName() + "', '"
                            + // name
                            f.getEmail() + "', '"
                            + // email
                            f.getBuilding() + "', "
                            + // buildingName
                            f.getOffice() + ", "
                            + // officeNumber
                            f.getPhone() + ", '"
                            + // phoneNumber
                            f.getDepartment() + "', '"
                            + // department
                            f.getPosition()
                            + // position
                            "')",
                            'u');
                }
            }

            for (Student s : students) {
                runDBQuery("SELECT * FROM JAVAUSER.STUDENT WHERE JAVAUSER.STUDENT.STUDENTID = " + s.getStuID(), 'r');

                if (jsqlResults.next()) {
                    runDBQuery("UPDATE Student SET fullName = '" + s.getFullName() + "', Address = '" + s.getAddress() + "', email = '" + s.getEmail() + "', gpa = " + s.getGPA() + ", ssn = " + s.getSSN() + " WHERE studentID = " + s.getStuID(), 'u');
                } else {
                    runDBQuery( // run
                            "INSERT INTO Student (studentID, fullName, Address, email, gpa, ssn) VALUES ("
                            + // Set up command
                            s.getStuID() + ", '"
                            + // studentID
                            s.getFullName() + "', '"
                            + // fullName
                            s.getAddress() + "', '"
                            + // Address
                            s.getEmail() + "', "
                            + // email
                            s.getGPA() + ", "
                            + // gpa
                            s.getSSN()
                            + // ssn
                            ")", 'u');
                }
            }

            for (Course c : courses) {
                runDBQuery("SELECT courseID FROM COURSE WHERE courseID = " + c.getID(), 'r');
                if (jsqlResults.next()) {
                    runDBQuery("UPDATE COURSE SET prefixs = '" + c.getPrefix() + "', courseNum = " + c.getNumber() + ", courseName = '" + c.getName() + "', days = '" + c.getDays() + "', startTime = '" + c.getStartTime() + "', endTime = '" + c.getEndTime() + "', creditHours = " + c.getCreditHours() + ", semesterID = " + c.getSemester().getID() + " WHERE courseID = " + c.getID(), 'u');
                } else {
                    runDBQuery( // run
                            "INSERT INTO COURSE (courseID, prefixs, courseNum, courseName, days, startTime, endTime, creditHours, semesterID) VALUES ("
                            + // Set up command
                            c.getID() + ", '"
                            + // courseID
                            c.getPrefix() + "', "
                            + // prefix
                            c.getNumber() + ", '"
                            + // number
                            c.getName() + "', '"
                            + // name
                            c.getDays() + "', '"
                            + // days
                            c.getStartTime() + "', '"
                            + // startTime
                            c.getEndTime() + "', "
                            + // endTime
                            c.getCreditHours() + ", "
                            + // creditHours
                            c.getSemester().getID()
                            + ")", 'u');
                }
            }

            runDBQuery(
                    "DELETE FROM SCHEDULE", 'd');
            for (Schedule s : schedules) {
                runDBQuery("Insert into Schedule (courseID, facultyID) Values ("
                        + s.getCourse().getID() + ", "
                        + s.getFaculty().getFacultyID()
                        + ")", 'u');
            }

            runDBQuery(
                    "DELETE FROM ENROLLMENT", 'd');
            for (Enrollment e : enrollments) {
                runDBQuery("Insert into Enrollment (courseID, StudentID) Values ("
                        + e.getCourse().getID() + ", "
                        + e.getStudent().getStuID()
                        + ")", 'u');
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void runDBQuery(String query, char queryType) {
        // queryType - Using the C.R.U.D. acronym
        // 'r' - SELECT
        // 'c', 'u', or 'd' - UPDATE, INSERT, DELETE

        try {
            String URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
            String user = "javauser";
            String pass = "javapass";

            oDS = new OracleDataSource();
            oDS.setURL(URL);

            jsqlConn = oDS.getConnection(user, pass);
            jsqlStmt = jsqlConn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            if (queryType == 'r') {
                jsqlResults = jsqlStmt.executeQuery();
            } else {
                jsqlStmt.executeUpdate();
            }
        } catch (SQLException sqlex) {
            System.out.println(sqlex.toString());
        }
    }
}
