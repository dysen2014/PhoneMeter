package com.dysen.myUtil.dl_notification;

public class UpdateInfo {
        private String version;
        private String pkgLen;
        private String description;
        private String url;

        public String getVersion()
        {
                return version;
        }
        public void setVersion(String version)
        {
                this.version = version;
        }

        public String getPkgLen() {
                return pkgLen;
        }

        public void setPkgLen(String pkgLen) {
                this.pkgLen = pkgLen;
        }

        public String getDescription()
        {
                return description;
        }
        public void setDescription(String description)
        {
                this.description = description;
        }
        public String getUrl()
        {
                return url;
        }
        public void setUrl(String url)
        {
                this.url = url;
        }

}
