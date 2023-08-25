package hr.java.vjezbe.sortiranje;

import hr.java.vjezbe.entitet.Student;

import java.util.Comparator;

public class StudentSorter implements Comparator<Student> {

    @Override
    public int compare(Student s1, Student s2) {
        if (s1.getPrezime().equals(s2.getPrezime())){
            return s1.getIme().compareTo(s2.getIme());
        }
        else{
            return s1.getPrezime().compareTo(s2.getPrezime());
        }
    }
}
