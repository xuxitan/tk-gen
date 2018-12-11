$(function () {
    $("#jqGrid").jqGrid({
        url: 'sys/generator/list',
        datatype: "json",
        colModel: [
            {label: '表名', name: 'tableName', width: 100, key: true},
            {label: 'Engine', name: 'engine', width: 70},
            {label: '表备注', name: 'tableComment', width: 100},
            {label: '创建时间', name: 'createTime', width: 100}
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50, 100, 200],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            tableName: null
        },
        projectName: "",
        packagePoName: "",
        basePackagePoName: "src/main/java",
        packageDaoName: "",
        basePackageDaoName: "src/main/java",
        packageMapperXmlName: "",
        basePackageMapperXmlName: "src/main/resources"
    },
    methods: {
        query: function () {
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'tableName': vm.q.tableName},
                page: 1
            }).trigger("reloadGrid");
        },
        generator: function () {
            var tableNames = getSelectedRows();
            if (tableNames == null) {
                return;
            }
            if (!this.projectName) {
                alert("项目目录不能为空");
                return false;
            }
            if (!this.packagePoName) {
                alert("实体类包名不能为空");
                return false;
            }
            if (!this.packageDaoName) {
                alert("mapper(dao)包名不能为空");
                return false;
            }
            if (!this.packageMapperXmlName) {
                alert("xml文件包名不能为空");
                return false;
            }
            this.projectName = this.projectName.replace(/\\/g, "/");
            location.href = "sys/generator/code?tables=" + tableNames.join() + "&projectName=" + this.projectName +
                "&packagePoName=" + this.packagePoName + "&basePackagePoName=" + this.basePackagePoName +
                "&packageDaoName=" + this.packageDaoName + "&basePackageDaoName=" + this.basePackageDaoName +
                "&packageMapperXmlName=" + this.packageMapperXmlName + "&basePackageMapperXmlName=" + this.basePackageMapperXmlName;

        }
    }
});

