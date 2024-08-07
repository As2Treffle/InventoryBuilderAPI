package fr.as2treffle.inventorybuilderapi.manager;

public enum ErrorType {
    INVENTORY_FILE_NOT_FOUND("Yaml file doesn't exists !"),
    INVENTORY_TYPE_ERROR("Inventory type not defined!"),
    INVENTORY_TYPE_SYNTAX_ERROR("Inventory type not exists!"),
    INVENTORY_SIZE_ERROR("Inventory size not valid!"),
    INVENTORY_SIZE_VALUE_ERROR("Inventory size must be a multiple of 9! : (9, 18, 27, 36, 45, 54)"),
    INVENTORY_TITLE_ERROR("Inventory title not defined!"),
    INVENTORY_SCHEMATIC_ERROR("Inventory schematic not defined!"),
    INVENTORY_BAD_SCHEMATIC_ERROR("Inventory schematic not defined correctly!"),
    ITEMSTACK_NOT_DEFINED("Itemstack with code: %arg% not defined!"),
    MATERIAL_ERROR("Material type with code %arg% doesn't exists"),
    ITEMSTACK_AMOUNT_NOT_DEFINED("Itemstack amount with code %arg% not defined!"),
    ADDON_NOT_FOUND("Addon with code %arg% not found!");

    private final String message;

    ErrorType(String s) {
        message = s;
    }

    public String getMessage() {
        return message;
    }
}
