
function get(key) {
  var  value = getApp().globalData[key] 
  if (!value) {
    value = wx.getStorageSync(key)
  }

  return value
}

function scanCode() {
  wx.scanCode({
    scanType: ["qrCode"],
    success: (res) => {
      console.log(res)
    },
  })
}

module.exports = {
  get,
  scanCode
}