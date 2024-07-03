import * as XLSX from 'xlsx';
import { ElError} from "@/util/MessageUtil";
/* 导出excel工具类 */
export default class ExcelUtil {
     private _sheetName: string = 'Sheet1';
     private readonly wb: XLSX.WorkBook;

    /* 创建excel对象 */
     constructor() {
         this.wb = XLSX.utils.book_new();
     }
    /* 先传入表名 */
    set sheetName(sheetName: string) {
        this._sheetName = sheetName ;
    }

    /* 将数据写入excel */
    public exportToExcel(header: string[], data: any[]): void {
        console.log(header)
        console.log(data)
        const ws : XLSX.WorkSheet  = XLSX.utils.json_to_sheet(data, { header: header });
        XLSX.utils.book_append_sheet(this.wb, ws, this._sheetName)
    }

    public downloadExcel(fileName: string = 'data'): void {
        try {
            /* 确保fileName不包含文件系统的无效字符 */
            fileName = fileName.replace(/[<>:"/\\|?*]/g, '');
            XLSX.writeFile(this.wb, `${fileName}.xlsx`);
        } catch (error) {
            ElError("下载Excel时出错:" + error) ;
            console.error('下载Excel时出错：', error);
        }
    }
}