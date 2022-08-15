package constants;

public class Constants {

    public static final String [] INVOICE_TABLE_COLS = new String [] {"No.", "Date", "Customer", "Total"};

    public static final String [] INVOICE_ITEMS_COLS = new String [] {"Item Name", "Item Price", "Count", "Item Total"};

    public static final int WINDOW_WIDTH = 1050;

    public static final int WINDOW_HEIGHT = 720;

    public static final String CREATE_BTN_ACTION_COMMAND = new String("create");

    public static final String DELETE_BTN_ACTION_COMMAND = new String("delete");

    public static final String SAVE_UPDATE_BTN_ACTION_COMMAND = new String("saveUpdate");

    public static final String ADD_ITEM_BTN_ACTION_COMMAND = new String("addFile");

    public static final String REMOVE_ITEM_BTN_ACTION_COMMAND = new String("remove");

    public static final String SAVE_FILE_ACTION_COMMAND = new String("saveFile");

    public static final String OPEN_FILE_ACTION_COMMAND = new String("openFile");

    public static final String INVOICE_DATA_EDITED_ACTION_COMMAND = new String("edit");

    public static final char SAVE_FILE_KEY_CODE = 'S';

    public static final char OPEN_FILE_KEY_CODE = 'O';
}
