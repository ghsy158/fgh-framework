package fgh.weixin.message.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import fgh.weixin.message.task.MessageTask;

public class TimerManager {

	private static final long PERIOD = 10 * 1000L;

	// private static final long PERIOD = 24 * 60 * 60 * 1000L;

	public TimerManager() {
		Calendar calender = Calendar.getInstance();
		calender.set(Calendar.HOUR_OF_DAY, 23);
		calender.set(Calendar.MINUTE, 20);
		calender.set(calender.SECOND, 0);

		Date date = calender.getTime();

		// 如果第一次执行定时任务的时间 小于 当前的时间
		// 此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。

		if (date.before(new Date())) {
			this.addDay(date, 1);
		}

		Timer timer = new Timer();
		MessageTask task = new MessageTask();
		timer.schedule(task, date, PERIOD);// 启动任务
	}

	// 增加或减少天数
	public Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}
}
