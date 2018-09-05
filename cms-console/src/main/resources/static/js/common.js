/**
 * layui 初始化分页表格
 * @param url
 * @param cols
 * @param where
 */
function initTable(url, cols, table, form) {

    //用户列表
    var tableIns = table.render({
        elem: '#tableId',
        url : url,
        page : true,
        id : "reloadTable",
        cols : cols
    });


    //监听搜索
    form.on('submit(submit-search)', function(data){
        var field = data.field;
        //执行重载
        table.reload('reloadTable', {
            where: field
        });
    });

    return tableIns;
}

// 窗口打开
function layer_show(title, url, w, h){
    if (w == null || w == '') {
        w=800;
    }
    if (h == null || h == '') {
        h=($(window).height() - 50);
    }
    var index = layer.open({
        type: 2,
        fix: false, //不固定
        resize:false,
        area : [w + "px", h + "px"],
        shade:0.4,
        title: title,
        content: url,
    })

}

// 全屏打开
function layer_show_full(title, url){
    var index = layer.open({
        type: 2,
        area : ['100%', '100%'],
        title: title,
        content: url
    });
    layer.full(index);
    window.sessionStorage.setItem("index",index);
    //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
    $(window).on("resize",function(){
        layui.layer.full(window.sessionStorage.getItem("index"));
    })
}

/*关闭弹出框口*/
function layer_close(){
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
}



