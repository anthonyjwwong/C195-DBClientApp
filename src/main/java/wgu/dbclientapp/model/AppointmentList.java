package wgu.dbclientapp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.IntStream;

/**
 * Appointment list to hold the appointment classes
 */
public class AppointmentList {
    private static ObservableList<Appointment> allAppointment = FXCollections.observableArrayList();

    /**
     * Add appointment into allAppointment list.
     * @param newAppointment appointment to add
     */
    public static void addAppointment(Appointment newAppointment) {
        allAppointment.add(newAppointment);
    }

    /**
     * Return all the appointment in the allAppointmentList
     * @return allAppointment list.
     */
    public static ObservableList<Appointment> getAllAppointment() {
        return allAppointment;
    }

    /**
     * Updates the appointment at a certain index of the allAppointment list
     * @param index index of the appointment to be updated
     * @param selectedAppointment appointment to be updated.
     */
    public static void updateList(int index, Appointment selectedAppointment) {
        allAppointment.set(index, selectedAppointment);
    }

    /**
     * Deletes an appointment from the list
     * @param selectedAppointment appointment to delete
     * @return true/false Deletes the appointment./
     */
    public static boolean deleteAppointment(Appointment selectedAppointment) {
        return allAppointment.remove(selectedAppointment);
    }

    /**
     * Finds the index of a certain appointment in the alAppointment list.
     * @param selectedAppointment
     * @return
     */
    public static int indexOf(Appointment selectedAppointment) {
        int index = 0;
        int size = allAppointment.size();

        for(index = 0; index<size; index++){
            if(selectedAppointment.equals(allAppointment.get(index))){
                return index;
            }
        }
        return index;
    }
}
