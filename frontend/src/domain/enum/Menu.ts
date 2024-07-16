export default class Menu {
    private _menuName: string;
    private _menuId: string;

    constructor(menuName: string, menuId: string) {
        this._menuName = menuName;
        this._menuId = menuId;
    }
    get menuName(): string {
        return this._menuName;
    }

    set menuName(value: string) {
       this._menuName = value;
    }

    get menuId(): string {
        return this._menuId;
    }

    set menuId(value: string) {
        this._menuId = value;
    }
}