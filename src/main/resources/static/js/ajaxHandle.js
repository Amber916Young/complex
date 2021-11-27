/**
 * ajax 方法
 */
function ajax(url, params, method, callBack) {
    $.ajax({
        url : url,
        type : method,
        data: params,
        headers: {
            "Authorization": "Basic " + btoa("admin" + ":" + "698d51a19d8a121ce581499d7b701668")
        },
        dataType : "json",
        success : function(json) {
            callBack(json);
        },
        error : function(json) {
            console.log("访问出错！");
            return false;
        }
    });
}

function adminReq(admin,url, params, method, callBack){
    admin.req({
        url: url //实际使用请改成服务端真实接口
        , data: params//这里把数据封装成json的了，在springmvc里面会自己解析。
        , datatype:'json'
        , method:method
        , done: function (json) {
            callBack(json);
        }
    });
}
/**
 * post 方法
 */
function ajaxPost(url, params, callBack) {
    ajax(url,params,"post",callBack);
}

/**
 * get 方法
 */
function ajaxGet(url, params, callBack) {
    ajax(url,params,"get",callBack);
}



/**
 * post 方法
 */
function ajaxPostAdmin(admin,url, params, callBack) {
    adminReq(admin,url,params,"post",callBack);
}

/**
 * get 方法
 */
function ajaxGetAdmin(admin,url, params, callBack) {
    adminReq(admin,url,params,"get",callBack);
}