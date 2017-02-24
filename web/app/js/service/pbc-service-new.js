var commonModule = require("../module/common-module");

module.exports = {
    getReportList: function (reportType, startDay, endDay) {
        var parameterList;
        parameterList = "start_day="+startDay+"&end_day="+endDay+"&report_type="+reportType;

        var data = {
            reportList: [],
            fileCount: 0
        };
        $.ajax({
            url: "/report/newpbc/list?"+parameterList,
            type: 'GET',
            async: false,
            dataType: 'json',
            success: function (response) {
                if (response.code == 200) {
                    data.reportList = response.data;
                }
            },
            error: function () {
                commonModule.errorModal("接口错误！");
            }
        });

        return data;
    },
    generateReport: function (reportList) {
        var result = false;

        $.ajax({
            url: "/report/newpbc/create",
            type: "POST",
            async: false,
            dataType: 'json',
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(reportList),
            success: function (data) {
                if (data.code == 200) {
                    commonModule.infoModal(data.message);
                    result = true;
                } else if (data.code == 400) {
                    commonModule.errorModal(data.message);
                }
            },
            error: function () {
                commonModule.errorModal("接口错误！");
            }
        });

        return result;
    },
    submitReport: function(startDay, endDay) {
        $.ajax({
            url: "/report/newpbc/submit?start_day="+startDay+"&end_day="+endDay,
            type: "POST",
            dataType: 'json',
            contentType: "application/json;charset=utf-8",
            success: function (data) {
                if (data.code == 200) {
                    commonModule.infoModal(data.message);
                } else if (data.code == 400) {
                    commonModule.errorModal(data.message);
                }
            },
            error: function () {
                commonModule.errorModal("接口错误！");
            }
        });
    },
    downloadable: function(reportType, startDay, endDay, reportName){
        var result = true;

        var url = null;
        url = "/report/newpbc/download?"
            +"&report_type="+reportType
            +"&start_day="+startDay
            +"&end_day="+endDay
            +"&report_name="+reportName;

        $.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            async: false,
            timeout : 5000,
            success: function (response) {
                if (response.code == 400) {
                    commonModule.errorModal(response.message);

                    result=false;
                }
            }
        });

        return result;
    },
    downloadableAll: function(startDay, endDay){
        var result = true;

        $.ajax({
            url: "/report/newpbc/download-all?"
            +"&start_day="+startDay
            +"&end_day="+endDay,
            type: 'GET',
            dataType: 'json',
            async: false,
            timeout : 5000,
            success: function (response) {
                if (response.code == 400) {
                    commonModule.errorModal(response.message);

                    result=false;
                }
            }
        });

        return result;
    }
}