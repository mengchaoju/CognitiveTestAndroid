package project.cognitivetest.modules;

/**
 * Created by 50650 on 2019/4/16
 */

public class Participant{
        private String participantID;
        private String firstName;
        private String familyName;
        private String gender;
        private String dateOfBirth;

        public Participant(String participantID, String firstName,
                           String familyName, String gender,
                           String dateOfBirth){
            this.participantID=participantID;
            this.firstName=firstName;
            this.familyName=familyName;
            this.gender=gender;
            this.dateOfBirth=dateOfBirth;
        }


        public String getParticipantID(){
            return this.participantID;
        }

        public void setParticipantID(String participantID) {
            this.participantID = participantID;
        }

        public String getFirstName(){
            return this.firstName;
        }

        public void setFirstName(String firstName){
            this.firstName=firstName;
        }

        public String getFamilyName(){
            return this.familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public void setGender(String gender){
            this.gender=gender;
        }

        public String getGender() {
            return this.gender;
        }

        public String getDateOfBirth(){
            return this.dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        @Override
        public String toString() {
            return "participants{"+
                    "participantid+'"+participantID+'\''+
                    ",firstname='"+firstName+'\''+
                    ",famliyname='"+familyName+'\''+
                    ",gender='"+gender+'\''+
                    ",dateofbirth='"+dateOfBirth+'\''+
                    '}';
        }


}
