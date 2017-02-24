var b2eService = require("../service/b2e-service");

module.exports = {
    getBankList: function(){
        return b2eService.getBankList();
    },
    getFrontServiceList: function(){
        return b2eService.getFrontServiceList();
    },
    getFrontServiceDetail: function (bankType) {
        return b2eService.getFrontServiceDetail(bankType);
    },
    getAccountList: function (bankType) {
        return b2eService.getAccountList(bankType);
    },
    getBankDetailList: function (bankType, startDay, endDay, accountNo, pageNo, pageSize) {
        return b2eService.getBankDetailList(bankType, startDay, endDay, accountNo, pageNo, pageSize);
    },
    getBankBalanceList: function (bankType, startDay, endDay, accountNo, pageNo, pageSize) {
        return b2eService.getBankBalanceList(bankType, startDay, endDay, accountNo, pageNo, pageSize);
    },
    getFrontServiceLog: function (bankType, date) {
        if (date==undefined || date==null || date==""){
            var dateObj = new Date();
            var year = dateObj.getFullYear();
            var month = dateObj.getMonth()+1;
            var day = dateObj.getDate();

            date = year+"-"+month+"-"+day;
        }
        return b2eService.getFrontServiceLog(bankType, date);
    }
};