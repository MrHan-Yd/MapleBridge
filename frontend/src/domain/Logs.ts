export interface Logs {
    tableName: string;
    tableType: string;
    engine: string;
    version: string;
    tableRows: string;
    avgRowLength: string;
    dataLength: string;
    indexLength: string;
    createTime: Date;
    updateTime: Date;
    tableCollation: string;
    tableComment: string;
}