package com.viasverdes.viasverdesespana.data.bo

data class ViaVerdeBO(val id: Long,
                      val codVV: String,
                      var name: String,
                      var length: Float,
                      var province: Array<ProvinceBO>,
                      var localization: String,
                      var naturaText: String,
                      var userType: Array<UserTypeBO>
)