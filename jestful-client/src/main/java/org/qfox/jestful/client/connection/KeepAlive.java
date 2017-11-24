package org.qfox.jestful.client.connection;

public enum KeepAlive {

    DEFAULT {
        @Override
        public void config(Connection connection) {
        }
    },
    ON {
        @Override
        public void config(Connection connection) {
            connection.getRequest().setRequestHeader("Connection", "keep-alive");
        }
    },
    OFF {
        @Override
        public void config(Connection connection) {
            connection.getRequest().setRequestHeader("Connection", "close");
        }
    };

    public abstract void config(Connection connection);

}
