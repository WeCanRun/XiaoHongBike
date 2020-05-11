// pages/register/register.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    flag : false,
    codeDis : false,
    phoneCode : "获取验证码",
    countryCodes: ["86", "80", "84", "87"],
    countryCodeIndex: 0,
    phoneNum : "",
    verifyCode : ""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  /**
   * 用户提交注册
   */
  formSubmit: function (e) {
    var countryCode = this.data.countryCodes[this.data.countryCodeIndex]
    var phoneNum = e.detail.value.phoneNum
    var verifyCode = e.detail.value.verifyCode
    wx.request({
      url: 'http://127.0.0.1:8080/user/genVerifyCode',
      method: 'GET',
      data: {
          countryCode: countryCode,
          phoneNum: phoneNum,
          verifyCode:verifyCode
      },
      success: function(res) {
        if(res.data) {
          wx.request({
            url: 'http://127.0.0.1:8080/user/register',
            method: 'POST',
            data: {
              countryCode: countryCode,
              phoneNum: phoneNum,
              regDate: new Date()
            },
            success:function(res) {
              console.log(res)
              if(res.data) {
                wx.navigateTo({
                  url: '../deposit/deposit',
                })
                //用户状态， 0未注册、1绑定手机号、2实名认证、
                getApp().globalData.status = 1
                getApp().globalData.phoneNum = phoneNum
                // 将用户信息保存到手机存储卡
                wx.setStorageSync("status", 1)
                wx.setStorageSync("phoneNum", phoneNum)
              } else {
                console.log(res.data)
                // 已经实名过了，可以开始用车
                // 调用扫码事件
                getApp().globalData.status = 3
                getApp().globalData.phoneNum = phoneNum
                // 将用户信息保存到手机存储卡
                wx.setStorageSync("status", 3)
                wx.setStorageSync("phoneNum", phoneNum)
              }
            }
          })
        } else {
          wx.showModal({
            title: '提示',
            content: "验证码错误，请稍后再试！",
          })
        }
      }
    })
   
  },

  /**
   * 获取验证码事件
   */
  getVerifyCode: function () {
    var that = this
    var countryCode = this.data.countryCodes[this.data.countryCodeIndex]
    var phoneNum = this.data.phoneNum
    if(phoneNum.length!= 11 || isNaN(phoneNum)){
        wx.showToast({
            title: '请输入有效的手机号码',
            icon : "loading"
        })
        setTimeout(function(){
            wx.hideToast()
        },2000)
        return
    }
    that.setData({
      codeDis : true
    })
    // 发送短信验证码
    wx.request({
      url: 'http://127.0.0.1:8080/user/get_verify_code',
      method: 'POST',
      data: {
        countryCode: countryCode,
        phoneNum: phoneNum
      },
      success: function(res) {
        var data = res.data
        console.log(data)
        if (data.code != "200") {
          that.setData({
            codeDis : false
           })
          wx.showToast({
              title: "获取验证码失败",
              icon : "loading"
          })
          setTimeout(function(){
              wx.hideToast()
          },2000)
        } else {
          wx.showToast({
            title: '验证码已发送',
            icon: 'success',
            duration: 2000
          })
          that.setData({
            phoneCode : 60
         })
         let time = setInterval(()=>{
             let phoneCode = that.data.phoneCode
             phoneCode --
             that.setData({
                 phoneCode : phoneCode
             })
             if(phoneCode == 0){
                  clearInterval(time)
                  that.setData({
                       phoneCode : "重新获取验证码",
                       flag : true,
                       codeDis: false,
                  })
             }
         },1000)
        }
      }
    })
  },

  bindCountryCodeChange: function(e) {
    let value = e.detail.value
    this.setData({
      countryCodeIndex:value
    })
  },

  phoneInput: function(e) {
    let value = e.detail.value
    this.setData({
      phoneNum:value
    })
  },

  codeInput: function(e) {
    let value = e.detail.value
    this.setData({
      verifyCode:value
    })
  }

})