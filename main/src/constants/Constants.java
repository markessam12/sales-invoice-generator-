package constants;

//class for all used constants in the code
public class Constants {

    public static final String [] INVOICE_TABLE_COLS = new String [] {"No.", "Date", "Customer", "Total"};

    public static final String [] INVOICE_ITEMS_COLS = new String [] {"Item Name", "Item Price", "Count", "Item Total"};

    public static final int WINDOW_WIDTH = 1050;

    public static final int WINDOW_HEIGHT = 720;

    public static final String CREATE_BTN_ACTION_COMMAND = "create";

    public static final String DELETE_BTN_ACTION_COMMAND = "delete";

    public static final String SAVE_UPDATE_BTN_ACTION_COMMAND = "saveUpdate";

    public static final String ADD_ITEM_BTN_ACTION_COMMAND = "addFile";

    public static final String REMOVE_ITEM_BTN_ACTION_COMMAND = "remove";

    public static final String SAVE_FILE_ACTION_COMMAND = "saveFile";

    public static final String OPEN_FILE_ACTION_COMMAND = "openFile";

    public static final String INVOICE_DATA_EDITED_ACTION_COMMAND = "edit";

    public static final char SAVE_FILE_KEY_CODE = 'S';

    public static final char OPEN_FILE_KEY_CODE = 'O';
}
