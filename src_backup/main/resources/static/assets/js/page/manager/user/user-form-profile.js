var userFormProfile = {

    profileUploader: new ThumbnailUpload(`#upload-container`)
    ,thumbnailImg: $("#profile-thumbnail-id")
    ,profileUid: null

    ,initUploader: function (uid = null) {
        userFormProfile.profileUid = uid;
        FileUtil.loadEditFiles(uid, userFormProfile.profileUploader);
        userFormProfile.profileUploader.setThumbNail("profile-thumbnail-id");
        userFormProfile.profileUploader.usePromise();
    }

    ,defaultProfile: async function () {
        const targets = [
            userFormProfile.profileUploader.getTempThumbnailFile(),
            userFormProfile.profileUploader.getThumbnailStaticUpload()
        ];
        for (const file of targets) {
            if (file.hasOwnProperty('fileId')) {
                const result = await userFormProfile.profileUploader.removeFilePromise(file, 'removed-by-user');
                if (result.submit) {
                    const obj = {
                        fileDel: [ result.file ],
                        profileUid: userFormProfile.profileUid
                    }
                    Http.put(`/cetus/api/user/change-profile/${uid}`, obj, true).then(() => {
                        Util.alert("프로필 이미지가 변경되었습니다.").then(() => {
                            window.location.href = `/manager/cetus/user/${uid}`
                        })
                    })
                }
            }
        }
    }

    ,newImageUpload: async function () {
        const result = await userFormProfile.profileUploader.openFileUploadPromise();
        if(result.submit) {
            const obj = {
                fileAdd: [ result.file ],
                profileUid: userFormProfile.profileUid
            }
            Http.put(`/cetus/api/user/change-profile/${uid}`, obj, true).then(() => {
                Util.alert("프로필 이미지가 변경되었습니다.").then(() => {
                    window.location.href = `/manager/cetus/user/${uid}`
                })
            })
        }
    }
}