import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

// ======================================
// Student Class
// ======================================
class Student {
    int rollNo;
    String name;
    String dob;
    String fatherName;
    String course;
    String branch;
    int year;
    String mobileNo;
    String email;
    String address;
    int semester;
    String[] subjects = new String[5];
    int[] marks = new int[5];

    Student(int rollNo, String name, String dob, String fatherName, String course, 
            String branch, int year, String mobileNo, String email, String address) {
        this.rollNo = rollNo;
        this.name = name;
        this.dob = dob;
        this.fatherName = fatherName;
        this.course = course;
        this.branch = branch;
        this.year = year;
        this.mobileNo = mobileNo;
        this.email = email;
        this.address = address;
        
        // Starting me subjects ko "NA" set kar dete hain
        for(int i = 0; i < 5; i++) {
            this.subjects[i] = "NA";
            this.marks[i] = 0;
        }
    }
}

// ======================================
// Main Class Start
// ======================================
public class StudentManagementSystem {

    static ArrayList<Student> students = new ArrayList<Student>();
    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME = "students.txt";
    static final String CSV_FILE_NAME = "Student_Report.csv";

    static final String ADMIN_USERNAME = "bablusingh";
    static final String ADMIN_PASSWORD = "bablu@123";

    // ======================================
    // Smart Input Validations (Basic Java Loops)
    // ======================================
    static String getValidMobile() {
        while (true) {
            System.out.print("Mobile No (10 Digits): ");
            String mobile = sc.nextLine().trim();
            
            // Pehle check karo length 10 hai ya nahi
            if (mobile.length() == 10) {
                boolean isAllDigits = true;
                // Loop chala kar ek ek character check karo ki vo number hai ya nahi
                for (int i = 0; i < mobile.length(); i++) {
                    char ch = mobile.charAt(i);
                   if (ch < '0' || ch > '9') {
                        isAllDigits = false;
                        break;
                    }
                }
                if (isAllDigits) {
                    return mobile;
                }
            }
            System.out.println("[✗] Error: Mobile number must be exactly 10 digits long.");
        }
    }

    static String getValidEmail() {
        while (true) {
            System.out.print("Email ID: ");
            String email = sc.nextLine().trim();
            // Ekdum basic check: contains '@' and '.'
            if (email.contains("@") && email.contains(".")) {
                return email;
            }
            System.out.println("[✗] Error: Invalid email format (e.g., alex@domain.com).");
        }
    }

    static String getValidDOB() {
        while (true) {
            System.out.print("DOB (DD-MM-YYYY): ");
            String dob = sc.nextLine().trim();
            // Basic length aur format check
            if (dob.length() == 10 && dob.charAt(2) == '-' && dob.charAt(5) == '-') {
                return dob;
            }
            System.out.println("[✗] Error: Invalid date format. Please use DD-MM-YYYY.");
        }
    }

    // ======================================
    // Secure Login System
    // ======================================
    static boolean adminLogin() {
        System.out.println("\n==================================");
        System.out.println("          SECURE ERP LOGIN        ");
        System.out.println("==================================");
        
        System.out.print("Enter Username: ");
        String username = sc.nextLine();
        
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("\n[✓] Login Successful! Access granted.");
            return true;
        } else {
            System.out.println("\n[✗] Invalid Username or Password! Access denied.");
            return false;
        }
    }

    // ======================================
    // Auto Roll Number Generation
    // ======================================
    static int getNextRollNumber() {
        if (students.isEmpty()) return 101; 
        int maxRoll = 0;
        for (Student st : students) {
            if (st.rollNo > maxRoll) {
                maxRoll = st.rollNo;
            }
        }
        return maxRoll + 1; 
    }

    // ======================================
    // Load Data From File
    // ======================================
    static void loadFromFile() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) return;
            
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 21) {
                    Student st = new Student(
                        Integer.parseInt(data[0]), data[1], data[2], data[3], data[4], 
                        data[5], Integer.parseInt(data[6]), data[7], data[8], data[9]
                    );
                    st.semester = Integer.parseInt(data[10]);
                    for (int i = 0; i < 5; i++) st.subjects[i] = data[11 + i];
                    for (int i = 0; i < 5; i++) st.marks[i] = Integer.parseInt(data[16 + i]);
                    students.add(st);
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error while loading student records!");
        }
    }

    // ======================================
    // Save All Data To File
    // ======================================
    static void saveToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));
            for (Student st : students) {
                String row = st.rollNo + "," + st.name + "," + st.dob + "," + 
                             st.fatherName + "," + st.course + "," + st.branch + "," + 
                             st.year + "," + st.mobileNo + "," + st.email + "," + 
                             st.address + "," + st.semester + ",";
                bw.write(row);

                for (int i = 0; i < 5; i++) {
                    bw.write((st.subjects[i] == null ? "NA" : st.subjects[i]) + ",");
                }
                for (int i = 0; i < 5; i++) {
                    bw.write(String.valueOf(st.marks[i]));
                    if (i < 4) bw.write(",");
                }
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Error while saving database files!");
        }
    }

    // ======================================
    // Search Student By Roll Number
    // ======================================
    static Student searchStudent(int rollNo) {
        for (Student st : students) {
            if (st.rollNo == rollNo) return st;
        }
        return null;
    }

    // ======================================
    // Smart Search (Partial Name & Branch)
    // ======================================
    static void smartSearch() {
        System.out.println("\n===== SMART SEARCH OPTIONS =====");
        System.out.println("1. Search by Roll Number");
        System.out.println("2. Search by Student Name");
        System.out.println("3. Filter by Branch");
        System.out.print("Enter your choice: ");
        int ch = sc.nextInt(); sc.nextLine();

        if (ch == 1) {
            System.out.print("Enter Roll No: ");
            int r = sc.nextInt(); sc.nextLine();
            Student st = searchStudent(r);
            if (st != null) printStudentDetails(st);
            else System.out.println("[✗] Student Not Found!");
        } 
        else if (ch == 2) {
            System.out.print("Enter student name: ");
            String searchName = sc.nextLine().toLowerCase();
            boolean found = false;
            for (Student st : students) {
                if (st.name.toLowerCase().contains(searchName)) {
                    printStudentDetails(st);
                    found = true;
                }
            }
            if (!found) System.out.println("[✗] No matching student records found.");
        } 
        else if (ch == 3) {
            System.out.print("Enter Branch Name (e.g., CSE): ");
            String branch = sc.nextLine();
            boolean found = false;
            for (Student st : students) {
                if (st.branch.equalsIgnoreCase(branch)) {
                    printStudentDetails(st);
                    found = true;
                }
            }
            if (!found) System.out.println("[✗] No records found matching this branch.");
        }
    }

    // ======================================
    // Export Data to CSV Report
    // ======================================
    static void exportToCSV() {
        if (students.isEmpty()) {
            System.out.println("[✗] No student data available to export!");
            return;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_NAME));
            bw.write("Roll No,Name,DOB,Father Name,Course,Branch,Year,Mobile,Email,Address,Semester,Total Marks,Percentage");
            bw.newLine();
            
            for (Student st : students) {
                int total = 0;
                for (int m : st.marks) total += m;
                double perc = total / 5.0;

                String row = st.rollNo + "," + st.name + "," + st.dob + "," + st.fatherName + "," +
                             st.course + "," + st.branch + "," + st.year + "," + st.mobileNo + "," +
                             st.email + "," + st.address + "," + st.semester + "," + total + "," + perc + "%";
                bw.write(row);
                bw.newLine();
            }
            bw.close();
            System.out.println("[✓] Report exported to " + CSV_FILE_NAME);
        } catch (Exception e) {
            System.out.println("[✗] Failure during CSV export.");
        }
    }

    // ======================================
    // Performance Analytics Dashboard
    // ======================================
    static void viewAnalytics() {
        if (students.isEmpty()) {
            System.out.println("[✗] Analytical pool is empty. Please add records first.");
            return;
        }
        int totalStudents = students.size();
        Student topper = students.get(0);
        int topperTotal = 0;
        int failedCount = 0;
        double batchTotalPercentage = 0;

        for (Student st : students) {
            int currentStudentTotal = 0;
            boolean failedInAny = false;
            for (int m : st.marks) {
                currentStudentTotal += m;
                if (m < 40) failedInAny = true; 
            }
            
            double currentPerc = currentStudentTotal / 5.0;
            batchTotalPercentage += currentPerc;

            if (currentStudentTotal > topperTotal) {
                topperTotal = currentStudentTotal;
                topper = st;
            }
            if (failedInAny || currentPerc < 40) {
                failedCount++;
            }
        }

        System.out.println("\n============================================");
        System.out.println("     DYNAMIC PERFORMANCE ANALYTICS DASHBOARD  ");
        System.out.println("============================================");
        System.out.println("Total Enrolled Students : " + totalStudents);
        System.out.println("Batch Average Score     : " + (batchTotalPercentage / totalStudents) + "%");
        System.out.println("College Topper          : " + topper.name + " (Roll No: " + topper.rollNo + ") with " + (topperTotal/5.0) + "%");
        System.out.println("Students Needing Focus  : " + failedCount);
        System.out.println("============================================");
    }

    // ======================================
    // Helper Method to Print Full Details
    // ======================================
    static void printStudentDetails(Student st) {
        System.out.println("\n----------------------------------");
        System.out.println("Roll No       : " + st.rollNo);
        System.out.println("Name          : " + st.name);
        System.out.println("DOB           : " + st.dob);
        System.out.println("Father Name   : " + st.fatherName);
        System.out.println("Course        : " + st.course);
        System.out.println("Branch        : " + st.branch);
        System.out.println("Year          : " + st.year);
        System.out.println("Mobile No     : " + st.mobileNo);
        System.out.println("Email         : " + st.email);
        System.out.println("Address       : " + st.address);
        System.out.println("Semester      : " + (st.semester == 0 ? "Not Assigned" : st.semester));
        System.out.println("----------------------------------");
    }

    // ======================================
    // Student Registration
    // ======================================
    static void registerStudent() {
        System.out.println("\n===== STUDENT REGISTRATION =====");
        int rollNo = getNextRollNumber();
        System.out.println("Assigned Roll No : " + rollNo + " (Auto)");

        System.out.print("Name : "); String name = sc.nextLine();
        String dob = getValidDOB();
        System.out.print("Father Name : "); String fatherName = sc.nextLine();
        System.out.print("Course : "); String course = sc.nextLine();
        System.out.print("Branch : "); String branch = sc.nextLine();
        System.out.print("Year : "); int year = sc.nextInt(); sc.nextLine();
        String mobileNo = getValidMobile();
        String email = getValidEmail();
        System.out.print("Address : "); String address = sc.nextLine();

        Student st = new Student(rollNo, name, dob, fatherName, course, branch, year, mobileNo, email, address);
        students.add(st);
        saveToFile();
        System.out.println("[✓] Student Registered Successfully!");
    }

    // ======================================
    // View All Students
    // ======================================
    static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("[✗] No Student Records Found!");
            return;
        }
        System.out.println("\n===== ALL REGISTERED STUDENTS =====");
        for (Student st : students) {
            printStudentDetails(st);
        }
    }

    // ======================================
    // Update Student
    // ======================================
    static void updateStudent() {
        System.out.print("\nEnter Roll No To Update : ");
        int rollNo = sc.nextInt(); sc.nextLine();

        Student st = searchStudent(rollNo);
        if (st == null) {
            System.out.println("[✗] Student Not Found!");
            return;
        }

        System.out.println("\n===== UPDATE STUDENT DETAILS =====");
        System.out.print("New Name : "); st.name = sc.nextLine();
        st.dob = getValidDOB();
        System.out.print("New Father Name : "); st.fatherName = sc.nextLine();
        System.out.print("New Course : "); st.course = sc.nextLine();
        System.out.print("New Branch : "); st.branch = sc.nextLine();
        System.out.print("New Year : "); st.year = sc.nextInt(); sc.nextLine();
        st.mobileNo = getValidMobile();
        st.email = getValidEmail();
        System.out.print("New Address : "); st.address = sc.nextLine();

        saveToFile();
        System.out.println("[✓] Student Updated Successfully!");
    }

    // ======================================
    // Delete Student
    // ======================================
    static void deleteStudent() {
        System.out.print("\nEnter Roll No To Delete : ");
        int rollNo = sc.nextInt();

        Student st = searchStudent(rollNo);
        if (st == null) {
            System.out.println("[✗] Student Not Found!");
            return;
        }
        students.remove(st);
        saveToFile();
        System.out.println("[✓] Student Deleted Successfully!");
    }

    // ======================================
    // Fill Exam Form
    // ======================================
    static void fillExamForm() {
        System.out.print("\nEnter Roll No : ");
        int rollNo = sc.nextInt(); sc.nextLine();

        Student st = searchStudent(rollNo);
        if (st == null) {
            System.out.println("[✗] Student Not Found!");
            return;
        }

        System.out.println("\n===== EXAM FORM =====");
        System.out.print("Enter Semester : ");
        st.semester = sc.nextInt(); sc.nextLine();

        for (int i = 0; i < 5; i++) {
            System.out.print("Enter Subject " + (i + 1) + " : ");
            st.subjects[i] = sc.nextLine();
        }
        saveToFile();
        System.out.println("[✓] Exam Form Submitted Successfully!");
    }

    // ======================================
    // Display Exam Form
    // ======================================
    static void displayExamForm() {
        System.out.print("\nEnter Roll No : ");
        int rollNo = sc.nextInt();

        Student st = searchStudent(rollNo);
        if (st == null) {
            System.out.println("[✗] Student Not Found!");
            return;
        }

        System.out.println("\n===== EXAM FORM DETAILS =====");
        System.out.println("Roll No   : " + st.rollNo);
        System.out.println("Name      : " + st.name);
        System.out.println("Semester  : " + st.semester);
        System.out.println("\nSubjects Listed:");
        for (int i = 0; i < 5; i++) {
            System.out.println((i + 1) + ". " + st.subjects[i]);
        }
    }

    // ======================================
    // Enter Marks
    // ======================================
    static void enterMarks() {
        System.out.print("\nEnter Roll No : ");
        int rollNo = sc.nextInt();

        Student st = searchStudent(rollNo);
        if (st == null) {
            System.out.println("[✗] Student Not Found!");
            return;
        }

        if (st.subjects[0] == null || st.subjects[0].equals("NA")) {
            System.out.println("[✗] Error: Please submit the Exam Form (Option 6) first.");
            return;
        }

        System.out.println("\n===== ENTER MARKS =====");
        for (int i = 0; i < 5; i++) {
            System.out.print(st.subjects[i] + " Marks (Out of 100): ");
            st.marks[i] = sc.nextInt();
        }
        saveToFile();
        System.out.println("[✓] Marks Saved Successfully!");
    }

    // ======================================
    // Generate Result
    // ======================================
    static void generateResult() {
        System.out.print("\nEnter Roll No : ");
        int rollNo = sc.nextInt();

        Student st = searchStudent(rollNo);
        if (st == null) {
            System.out.println("[✗] Student Not Found!");
            return;
        }

        int total = 0;
        for (int i = 0; i < 5; i++) total += st.marks[i];
        double percentage = total / 5.0;
        String grade;

        if (percentage >= 80) grade = "A";
        else if (percentage >= 60) grade = "B";
        else if (percentage >= 40) grade = "C";
        else grade = "F";

        String result = (percentage >= 40) ? "PASS" : "FAIL";

        System.out.println("\n===== ACADEMIC PERFORMANCE REPORT =====");
        System.out.println("Roll No : " + st.rollNo + " | Name : " + st.name);
        System.out.println("Semester : " + st.semester + " | Branch: " + st.branch);
        System.out.println("---------------------------------------");
        for (int i = 0; i < 5; i++) {
            System.out.println(st.subjects[i] + " \t: " + st.marks[i]);
        }
        System.out.println("---------------------------------------");
        System.out.println("Total Marks : " + total + "/500");
        System.out.println("Percentage  : " + percentage + "%");
        System.out.println("Final Grade : " + grade);
        System.out.println("Status      : " + result);
    }

    // ======================================
    // Main Method
    // ======================================
    public static void main(String[] args) {
        loadFromFile();
        
        if (!adminLogin()) {
            System.out.println("System Closing.");
            return; 
        }

        int choice;
        do {
            System.out.println("\n=============================================");
            System.out.println("     SMART ERP STUDENT MANAGEMENT SYSTEM      ");
            System.out.println("=============================================");
            System.out.println("1. Register New Student (Auto Roll No)");
            System.out.println("2. View All Student Data");
            System.out.println("3. Smart Search & Filters");
            System.out.println("4. Update Student Information");
            System.out.println("5. Delete Student Record");
            System.out.println("6. Fill Academic Exam Form");
            System.out.println("7. Display Exam Form");
            System.out.println("8. Enter Subject Marks");
            System.out.println("9. Generate Student Result");
            System.out.println("10. View Batch Analytics Dashboard");
            System.out.println("11. Export All Reports to Excel (CSV)");
            System.out.println("12. Exit System");
            System.out.print("Please enter a valid option (1-12): ");
            
            choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1: registerStudent(); break;
                case 2: viewAllStudents(); break;
                case 3: smartSearch(); break;
                case 4: updateStudent(); break;
                case 5: deleteStudent(); break;
                case 6: fillExamForm(); break;
                case 7: displayExamForm(); break;
                case 8: enterMarks(); break;
                case 9: generateResult(); break;
                case 10: viewAnalytics(); break;
                case 11: exportToCSV(); break;
                case 12: System.out.println("Thank You for Using Smart ERP System"); break;
                default: System.out.println("[✗] Invalid Choice! Try again.");
            }
        } while (choice != 12);
    }
}