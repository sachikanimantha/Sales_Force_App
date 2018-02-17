package com.example.bellvantage.scadd;

/**
 * Created by Bellvantage on 23/05/2017.
 */


public class IRA_PrintCommand {
    public static final byte[] INIT = {27, 64};
    public static byte[] FEED_LINE = {10};

    public static byte[] SELECT_FONT_A = {27, 33, 0};

    public static byte[] SET_BAR_CODE_HEIGHT = {29, 104, 100};
    public static byte[] PRINT_BAR_CODE_1 = {29, 107, 2};
    public static byte[] SEND_NULL_BYTE = {0x00};

    public static byte[] SELECT_PRINT_SHEET = {0x1B, 0x63, 0x30, 0x02};
    public static byte[] FEED_PAPER_AND_CUT = {0x1D, 0x56, 66, 0x00};

    public static byte[] SELECT_CYRILLIC_CHARACTER_CODE_TABLE = {0x1B, 0x74, 0x11};

    public static byte[] SELECT_BIT_IMAGE_MODE = {0x1B, 0x2A, 33,(byte) 164,1}; //last 2 depend on picture width,256x256 - 0,1
    //192x60 - 192,0
    //288x97 - 32,1
    //420x141 -     164,1
    //actually  0 < 4th one < 256
    // 0 =< 5th one =< 3

    public static byte[] SET_LINE_SPACING_24 = {0x1B, 0x33, 24};
    public static byte[] SET_LINE_SPACING_30 = {0x1B, 0x33, 30};

    public static byte[] SET_RIGHTSIDE_CHARCTOR_SIZE = {0x1B, 0x20, 1};

    public static byte[] TRANSMIT_DLE_PRINTER_STATUS = {0x10, 0x04, 0x01};
    public static byte[] TRANSMIT_DLE_OFFLINE_PRINTER_STATUS = {0x10, 0x04, 0x02};
    public static byte[] TRANSMIT_DLE_ERROR_STATUS = {0x10, 0x04, 0x03};
    public static byte[] TRANSMIT_DLE_ROLL_PAPER_SENSOR_STATUS = {0x10, 0x04, 0x04};
}
