package com.aviccii.cc.utils;

/**
 * @author aviccii 2020/9/21
 * @Discrimination
 */
public class IdWorker {

    /**
     * 开始时间戳
     */
    private final long twepoch = 1420041600000L;

    /**
     * 机器ID所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBits = 5L;

    /**
     * 支持最大的机器id,结果是31（这个移位算法可以很快的j计算出几位二进制数所能表示的最大二进制数）
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大的数据标识id,结果是31
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 序列在id中所占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器id向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位（12+5）
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间轴向左移22位（5+5+12）
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095(0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器id
     */
    private long workerId;

    /**
     * 数据中心id
     */
    private long datacenterId;

    /**
     * 毫秒内数列
     */
    private long sequence = 0L;

    /**
     * 上次生成id的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     */

    public IdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获得下一个id
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次id生成的时间戳，说明系统时钟回退过这个时候应该抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp)
            );
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        }

        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成Id的时间戳
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起时组成64位的id
        return ((timestamp - twepoch) << timestampLeftShift)
                | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     */
    protected long tilNextMillis(long lastTimestamp){
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp){
            timestamp= timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     */
    protected long timeGen(){
        return System.currentTimeMillis();
    }

}
