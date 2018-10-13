var $,tab,dataStr,layer;
layui.config({
	base :  "static/js/"
}).extend({
	"bodyTab" : "bodyTab"
})
layui.use(['bodyTab','form','element','layer','jquery'],function(){
	var form = layui.form,
		element = layui.element;
		$ = layui.$;
    	layer = parent.layer === undefined ? layui.layer : top.layer;
		tab = layui.bodyTab({
			openTabNum : "50",  //最大可打开窗口数量
			//url : "/static/json/navs.json" //获取菜单json地址
		});

	//通过顶部菜单获取左侧二三级菜单   注：此处只做演示之用，实际开发中通过接口传参的方式获取导航数据
	/*function getData(json){
		$.getJSON(tab.tabConfig.url,function(data){
			if(json == "contentManagement"){
				dataStr = data.contentManagement;
				//重新渲染左侧菜单
				tab.render();
			}else if(json == "memberCenter"){
				dataStr = data.memberCenter;
				//重新渲染左侧菜单
				tab.render();
			}else if(json == "systemeSttings"){
				dataStr = data.systemeSttings;
				//重新渲染左侧菜单
				tab.render();
			}else if(json == "seraphApi"){
                dataStr = data.seraphApi;
                //重新渲染左侧菜单
                tab.render();
            }
		})
	}*/
	//页面加载时判断左侧菜单是否显示
	//通过顶部菜单获取左侧菜单
	/*$(".topLevelMenus li,.mobileTopLevelMenus dd").click(function(){
		if($(this).parents(".mobileTopLevelMenus").length != "0"){
			$(".topLevelMenus li").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
		}else{
			$(".mobileTopLevelMenus dd").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
		}
		$(".layui-layout-admin").removeClass("showMenu");
		$("body").addClass("site-mobile");
		getData($(this).data("menu"));
		//渲染顶部窗口
		tab.tabMove();
	})*/

    // 如果没有头像使用默认头像
    if($(".userAvatar").attr("src") == null || $(".userAvatar").attr("src") == '') {
        $(".userAvatar").attr("src","static/images/avatar.jpg");
    }

	//通过顶部菜单获取左侧二三级菜单   注：此处只做演示之用，实际开发中通过接口传参的方式获取导航数据
	//getData("contentManagement");

	//手机设备的简单适配
    $('.site-tree-mobile').on('click', function(){
		$('body').addClass('site-mobile');
	});
    $('.site-mobile-shade').on('click', function(){
		$('body').removeClass('site-mobile');
	});

	// 添加新窗口
	$("body").on("click",".layui-nav .layui-nav-item a:not('.mobileTopLevelMenus .layui-nav-item a')",function(){
		//如果不存在子级
		if($(this).siblings().length == 0){
			addTab($(this));
			$('body').removeClass('site-mobile');  //移动端点击菜单关闭菜单层
		}
		$(this).parent("li").siblings().removeClass("layui-nav-itemed");
	})

})

//打开新窗口
function addTab(_this){
	tab.tabAdd(_this);
}

//捐赠弹窗
function donation(){
	layer.tab({
		area : ['320px', '480px'],
		tab : [{
			title : "微信",
			content : "<div style='padding:30px;overflow:hidden;background:#d2d0d0;'><img width='100%' height='100%' class='code' src='static/images/money/wechat_charge_code.jpg'></div>"
		},{
			title : "支付宝",
			content : "<div style='padding:30px;overflow:hidden;background:#d2d0d0;'><img width='100%' height='100%' src='static/images/money/alipay_charge_code.jpg'></div>"
		}]
	})
}
