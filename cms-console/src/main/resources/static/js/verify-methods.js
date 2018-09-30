layui.use(['form','layer'],function(){
    var form = layui.form;
    form.verify({
        phone_a: function(value, item){ //value：表单的值、item：表单的DOM对象
            if(value != null && value !== '' && !/^1\d{10}$/.test(value)){
                return '请输入正确的手机号';
            }
        },
        email_a : function (value, item) {
            if(value != null && value !== '' && !/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/.test(value)){
                return '邮箱格式不正确';
            }
        }
    });
});