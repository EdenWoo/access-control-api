package com.cfgglobal.test.exceptions

import lombok.Data

@Data
class ApiResp {
    var code: Int? = null
        set(code) {
            field = this.code
        }

    var message: String? = null
        set(message) {
            field = this.message
        }

    var error: String? = null
        set(error) {
            field = this.error
        }

}

