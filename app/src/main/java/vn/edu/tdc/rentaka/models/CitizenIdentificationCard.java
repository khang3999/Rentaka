package vn.edu.tdc.rentaka.models;

public class CitizenIdentificationCard extends Identification{

        public CitizenIdentificationCard(String image, String fullName, String dateIssued, String number) {
            super(image, fullName, dateIssued, number);
        }

        public CitizenIdentificationCard() {
            super();
        }
}
