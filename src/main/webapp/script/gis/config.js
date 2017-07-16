var config = function() {
    var serverIp = "192.168.0.203";
    var tomcatIP = "192.168.0.112";
    var locaServerIp = "192.168.0.203";
    var dbIp = "192.168.0.203";

    var serverPort = "8080";
    var tomcatPort = "8080";
    var locaServerPort = "1500";

    var dbUserName = "ningbo";
    var dbPassword = "ningbo";
    var groundLayerName = "180fd";

    var dbServer = dbIp + "/nbdb";
    var deployName = "/alarm";

    var defaultRelativeDir = "http://" + serverIp + ":" + serverPort + "/locaspace/gcm/";

    var localhost = "http://" + tomcatIP + ":" + tomcatPort;

    return {
        getServerIp : function() {
            return serverIp;
        },

        getTomcatIP : function() {
            return tomcatIP;
        },

        getLocaServerIp : function() {
            return locaServerIp;
        },

        getDbIp : function() {
            return dbIp;
        },

        getServerPort : function() {
            return serverPort;
        },

        getTomcatPort : function() {
            return tomcatPort;
        },

        getLocaServerPort : function() {
            return locaServerPort;
        },

        getDbUserName : function() {
            return dbUserName;
        },

        getDbPassword : function() {
            return dbPassword;
        },

        getGroundLayerName : function() {
            return groundLayerName;
        },

        getDbServer : function() {
            return dbServer;
        },

        getDeployName : function() {
            return deployName;
        },

        getDefaultRelativeDir : function() {
            return defaultRelativeDir;
        },

        getLocalhost : function() {
            return localhost;
        }
    }
} ();