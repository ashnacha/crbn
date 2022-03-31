package csci499.crbn;

public class sharedData {
    private static String curr_email;
    public static String getCurr_email() {return curr_email;}
    public static void setCurr_email(String curr_email) {
        sharedData.curr_email= curr_email;}
}
