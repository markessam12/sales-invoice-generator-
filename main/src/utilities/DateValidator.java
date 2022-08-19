package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValidator{
    public static boolean validateJavaDate(String strDate)
    {
        // Checking if date is 'null'
        if (!strDate.trim().equals("")) {
            //Date format used in example files
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            dateFormat.setLenient(false);
            try {
                Date testedDate = dateFormat.parse(strDate);
                Date date = new Date();
                if (date.before(testedDate)) {
                    return true;
                }
            }
            // Date format is invalid
            catch (ParseException e) {
                return true;
            }
            // Return true if date format is valid
        }
        return false;
    }
}
