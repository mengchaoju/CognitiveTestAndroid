package project.cognitivetest.modules;

import java.util.ArrayList;

/**
 * Created by 50650 on 2019/4/16
 */
public class ParticipantBean {
    public String resultcode;
    public String error_code;
    public String reason;

    public ArrayList<participant> result;

    public class participant{
        private String participantID;
        private String firstName;
        private String familyName;
        private String gender;
        private String dateOfBirth;


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
            return gender;
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

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setResult(ArrayList<participant> result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public String getReason() {
        return reason;
    }

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public ArrayList<participant> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "ParticipantBean{" +
                "resultcode='" + resultcode + '\'' +
                ", error_code='" + error_code + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
