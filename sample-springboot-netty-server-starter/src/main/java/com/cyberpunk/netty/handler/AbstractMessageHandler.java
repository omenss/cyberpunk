package com.cyberpunk.netty.handler;

/**
 * @author lujun
 * @date 2022/9/7 11:24
 */
public abstract class AbstractMessageHandler {

    /**
     * 责任链，下一个链接节点
     */
    protected AbstractMessageHandler next = null;


    /**
     * 具体逻辑
     *
     * @param map 客户端发送的消息
     * @return 处理结果
     */
    public abstract String doHandler(String message);

    public void next(AbstractMessageHandler handler) {
        this.next = handler;
    }


    public static class Builder<T> {
        private AbstractMessageHandler head;
        private AbstractMessageHandler tail;

        public Builder<T> addHandler(AbstractMessageHandler handler) {
            if (this.head == null) {
                this.head = handler;
            } else {
                this.tail.next(handler);
            }
            this.tail = handler;
            return this;
        }

        public AbstractMessageHandler build() {
            return this.head;
        }
    }

}
