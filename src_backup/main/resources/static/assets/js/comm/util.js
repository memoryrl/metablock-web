class Util {

    static get$(idOrObj) {
        return (idOrObj instanceof $ ? idOrObj : $('#' + idOrObj))
    }

    static alert(html) {
        return Swal.fire({
            html,
            buttonsStyling: false,  /* 버튼 기본 스타일을 이용하지 않고, 사용자의 고유 클래스 사용 (기본: true) */
            customClass: {
                confirmButton: 'btn-confirm-control'
            },
            confirmButtonText: '확인'
        })
    }

    static confirm(html, confirmButtonText = '확인', cancelButtonText = '취소') {
        return Swal.fire({
            html,
            buttonsStyling: false, /* 버튼 기본 스타일을 이용하지 않고, 사용자의 고유 클래스 사용 (기본: true) */
            customClass: {
                confirmButton: 'btn-confirm-control',
                cancelButton: 'btn-cancel-control'
            },
            showCancelButton: true,
            confirmButtonText,
            cancelButtonText
        }).then((res) => res.isConfirmed)
    }

    /*
    * 참고 : https://sweetalert2.github.io/#input-types
    * */
    static confirmTextArea(title, inputLabel) {
        return Swal.fire({
            input: "textarea",
            title: title,                                       // main title
            inputLabel: inputLabel,                             // sub title
            inputPlaceholder: "Type your message here...",      // [textarea] placeholder
            showCancelButton: true,
            allowOutsideClick: false,                           // 외부 요소를 통한 모달 닫기 false
            allowEscapeKey: false,                              // [ESC]를 통한 모달 닫기 false
            customClass: {
                confirmButton: 'btn-confirm-control',
                cancelButton: 'btn-cancel-control'
            },
            inputValidator: (value) => {        // [confirm] 버튼을 누르게 되면 validation 체크
                if(!value) {
                    return "You need to write something!!";
                }
            }
        }).then((res) => {
            return { isOk: res.isConfirmed, value: res.value ? res.value : '' }     // { isOk: 확인 체크 여부, value: input-value }
        })
    }
}


class Http {

    constructor() {
        this.headers = {
            'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        };
    }

    static handleError(e) {
        if (e.status === 401) {
            Util.alert("다시 로그인 해주세요.").then(() => {
                window.location.href = e.redirect;
            })
        } else {
            console.error('AJAX Error: ', e);
        }
    }

    static getJson(url, method = 'GET') {
        return $.ajax({
            url,
            method,
            headers: this.headers,
            dataType: "json",
            contentType: "application/json",
            v: Date.now(),
        });
    }

    static get(url, params= '', method = 'GET') {
        return $.ajax({
            url,
            method,
            headers: this.headers,
            data: params,
            v: Date.now(),
        })
        .fail(e => this.handleError(e))
    }

    static blobGet(url, params= '', method = 'GET') {
        return $.ajax({
            url,
            method,
            headers: this.headers,
            data: params,
            v: Date.now(),
            xhrFields:{
                responseType: 'blob'
            },
        })
        .fail(e => this.handleError(e))
    }

    static getSync(url, params= '', method = 'GET') {
        let result=null;
        $.ajax({
            url,
            method,
            headers: this.headers,
            data: params,
            v: Date.now(),
            async: false,
            success: function(data){
                result = data;
            }
        });
        return result;
    }

    static post(url, data, body = false, method = 'POST') {
        return $.post({
            url,
            method,
            data: body ? JSON.stringify(data) : data,
            headers: this.headers,
            contentType: body
                ? 'application/json;'
                : 'application/x-www-form-urlencoded;',
        })
        // .catch(e => this.handleError(e))
    }

    static postData(url, formData, method = 'POST') {
        return $.ajax({
            url,
            method,
            headers: this.headers,
            data: formData,
            cache: false,
            contentType: false,
            processData: false
        })
        .fail(e => this.handleError(e))
    }

    static put(url, data) {
        return Http.post(url, data, true, 'PUT')
    }

    static patch(url, data) {
        return Http.post(url, data, true, 'PATCH')
    }

    static delete(url, data) {
        return Http.get(url, data, 'DELETE')
    }

    static excel(url, params, fileName) {
        return $.ajax({
            url,
            method: 'GET',
            xhrFields: {
                responseType: 'blob',
            },
            data: params,
            success: function(response) {
                const link = document.createElement('a');
                link.href = URL.createObjectURL(response);
                link.download = fileName;
                link.click();
            },
            error: function(error) {
                console.log("Error in downloading the Excel file: ", error);
            }
        })
        .fail(e => this.handleError(e));
    }
}

class DateUtil {

    /**
     * 입력: 2025-01-12
     * 출력: year=2025 / month=Jan / day=12
     * */
    static dateToEng(dateString) {
        const date = new Date(dateString)
        const options = { year: 'numeric', month: 'short', day: '2-digit' }
        const formattedDate = date.toLocaleDateString('en-US', options)
        const [month, day, year] = formattedDate.split(/[\s,]+/)
        return { year, month, day };
    }
}

class Switch {

    static isChecked(id) {
        return Util.get$(id).is(':checked')
    }

    static check(id, flag = true) {
        const checked = Switch.isChecked(id)
        if ( checked !== flag ){
            Util.get$(id).click()
        }
    }
}

class Common {

    static isChecked(id) {
        return Util.get$(id).is(':checked')
    }

    static check(id, flag = true) {
        const checked = Switch.isChecked(id)
        if ( checked !== flag ){
            Util.get$(id).click()
        }
    }

    /**
     * Set the rating star UI based on the grade.
     * 
     * @param {jQuery} targetEle - The jQuery element containing the stars.
     * @param {number} idxGrade - The grade (0 to 5).
     */
    static setRattingStar(targetEle, idxGrade) {

        idxGrade = Number(idxGrade);

        targetEle.each(function (i) {

            if (i < idxGrade) {

                $(this).addClass("filled");
            }
            else if (idxGrade === 0) {

                targetEle.removeClass("filled");
            } 
            else {

                $(this).removeClass("filled");
            }

            $(this).parent().attr("data-value", idxGrade);
        });
    }

    /**
     * Get the rating value from the stars UI.
     * 
     * @param {jQuery} targetEle - The jQuery element containing the stars.
     * @returns {number} The rating value (0 to 5).
     */
    static getRattingStar(targetEle){

        let idxGrade = 0;

        if(typeof targetEle.attr("data-value") !== "undefined"){

            idxGrade = Number(targetEle.attr("data-value"));
        }

        return idxGrade;
    }

     /**
     * Apply a click event to rating stars to handle user interaction.
     * 
     * @param {jQuery} targetEle - The jQuery element containing the stars.
     */
    static setRattingStarOnClick(targetEle){

        targetEle.on("click", function () {

            let idx = $(this).index();
            
            // 동일한 평점 한번더 클릭시 적용 취소 처리
            if($(this).parent().attr("data-value") === String(idx + 1) && $(this).hasClass("filled")){

                idx = 0;
            }
            else {

                idx += 1;
            }

            Common.setRattingStar(targetEle, idx);
        });
    }
}

class FormDataToObj {
    static async getParameter(formId) {
        const form = document.getElementById(formId);
        if (!form) {
            console.error("Form not found with id: " + formId);
            return;
        }
        const formData = new FormData(form);
        const data = Array.from(formData.entries()).reduce((perv, [key, value]) => {
            if (perv[key]) {
                Array.isArray(perv[key]) ? perv[key].push(value) : (perv[key] = [perv[key], value]);
            } else {
                perv[key] = value;
            }
            return perv;
        }, {});
        return data;
    }

    /**
     * form 안에 해당하는 값들 가져오는데, 빈값이면 제외
     * */
    static async getParameterWithOutBlank(formId) {
        const form = document.getElementById(formId);
        if (!form) {
            console.error("Form not found with id: " + formId);
            return;
        }
        const formData = new FormData(form);
        const data = Array.from(formData.entries()).reduce((prev, [key, value]) => {
            if (value === "" || value === null) return prev;
            if (prev[key]) {
                Array.isArray(prev[key]) ? prev[key].push(value) : (prev[key] = [prev[key], value]);
            } else {
                prev[key] = value;
            }
            return prev;
        }, {});
        return data;
    }
}

class ValidationError {

    static setValidationMssg(formId, obj, isParent = false) {
        if(isParent) {
            obj = Object.fromEntries(Object.entries(obj).map(([k, v]) => [k.includes(".") ? k.split(".").pop() : k, v]));
        }

        const form = document.getElementById(formId);
        if (!form) {
            console.error("Form not found with id: " + formId);
            return;
        }

        form.querySelectorAll('[name]').forEach(element => {
            const name = element.name;

            // 1. {element} validation error 발생
            if (obj.hasOwnProperty(name)) {
                // 1-a. {is-invalid} 클래스 추가
                element.classList.add('is-invalid');

                // 1-b. ${name}-error 메시지 출력
                let errorElement = document.getElementById(`${name}-error`);
                if (errorElement) errorElement.textContent = obj[name];
                else {
                    errorElement = document.createElement('span');
                    errorElement.classList.add('validation-error-message');
                    errorElement.id = `${name}-error`;
                    errorElement.textContent = obj[name];
                    element.parentNode.appendChild(errorElement);
                }

                // 2. {element} validation error 없음
            } else {

                element.classList.remove('is-invalid');
                const errorElement = document.getElementById(`${name}-error`);
                if (errorElement) {
                    errorElement.textContent = '';
                }
            }
        });
    }

    static clearValidationMssg(formId) {
        const form = document.getElementById(formId);
        if (!form) {
            console.error("Form not found with id: " + formId);
            return;
        }

        form.querySelectorAll('[name]').forEach(element => {
            element.classList.remove('is-invalid');

            const errorElement = document.getElementById(`${element.name}-error`);
            if (errorElement) {
                errorElement.textContent = '';
                errorElement.remove();
            }
        });
    }
}