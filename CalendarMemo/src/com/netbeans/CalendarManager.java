package com.netbeans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * util class
 * @author emma
 */
public class CalendarManager {
	static final int CAL_WIDTH = 7; //日历显示列
	static final int CAL_HEIGHT = 6;//日历显示行
	int[][] calDates = new int[CAL_HEIGHT][CAL_WIDTH];//要显示的日期数组
	int calYear;//要显示的日期年
	int calMonth;//要显示的日期月
	int calDayOfMon;//要显示的日期日
	int calLastDate;//要显示的当月最后一天
	JLabel dateLabel;//左侧日历上方可变的标签
        JLabel fileInfoLabel;//右上角文件信息的显示
        JLabel pointLabel;//文本区域上方标签
        JTextArea memoArea;//右侧文本区域
	final int[] calLastDateOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };//每月最后一天
	
	/**
	 * init today date
	 */
	public void initToday(JButton[][] dateButs, JTextArea memoArea) {
		Calendar today = Calendar.getInstance();
		this.calYear = today.get(1);
		this.calMonth = today.get(2);
		this.calDayOfMon = today.get(5);
		makeCalData(today);
		showCal(dateButs);
		readMemo();
	}
        
        /**
	 * return to today
	 */
	public void setToday(JButton[][] dateButs) {
		Calendar today = Calendar.getInstance();
		this.calYear = today.get(1);
		this.calMonth = today.get(2);
		this.calDayOfMon = today.get(5);
		makeCalData(today);
		showCal(dateButs);
                 dateLabel.setText("<html><table width=100><tr><th><font size=5>" + ((calMonth + 1 < 10) ? "&nbsp;" : "")
						+ (calMonth + 1) + " / " +  calYear + "</th></tr></table></html>");
	}
	
	/**
	 * init the calendar 
	 * @param cal
	 */
	private void makeCalData(Calendar cal) {
            /**
             *  2018-9-20
             *  是周四 。 Calendar.DAY_OF_WEEK = 5
             * （5 + 7 - 20 % 7）% 7 = 6
             */
		int calStartingPos = (cal.get(Calendar.DAY_OF_WEEK) + 7 - (cal.get(Calendar.DAY_OF_MONTH) % 7)) % 7;//拿到指定cal起始星期数
		if (this.calMonth == 1)
			this.calLastDate = (this.calLastDateOfMonth[this.calMonth] + leapCheck(this.calYear)); //闰年当月最后一天是29天
		else
			this.calLastDate = this.calLastDateOfMonth[this.calMonth];

		//init the value to 0
		for (int i = 0; i < 6; ++i) {
			for (int j = 0; j < 7; ++j) {
				this.calDates[i][j] = 0;
			}
		}

		int i = 0;
		int num = 1;
		//补充每天数据，如果i=0表示为第一周，需要从calStartingPos开始计算
		for (int k = 0; i < 6; ++i) {
			if (i == 0)
				k = calStartingPos;
			else
				k = 0;
			for (int j = k; j < 7; ++j) {
				if (num > this.calLastDate)
					continue;
				this.calDates[i][j] = (num++);
			}
		}
	}
	
	/**
	 * 显示日历,每次更新日历信息
	 * @param dateButs
	 */
	public void showCal(JButton[][] dateButs) {
		Calendar today = Calendar.getInstance();
		for (int i = 0; i < 6; ++i)
			for (int j = 0; j < 7; ++j) {
				String fontColor = "black";
				if (j == 0)
					fontColor = "red";
				else if (j == 6)
					fontColor = "blue";

				//读取备忘录数据，有数据，当天日期加粗变黑
				File f = new File("MemoData/" + this.calYear + ((this.calMonth + 1 < 10) ? "0" : "")
						+ (this.calMonth + 1) + ((this.calDates[i][j] < 10) ? "0" : "") + this.calDates[i][j] + ".txt");
				if (f.exists())
					dateButs[i][j].setText(
							"<html><b><font color=" + fontColor + ">" + this.calDates[i][j] + "</font></b></html>");
				else {
					dateButs[i][j]
							.setText("<html><font color=" + fontColor + ">" + this.calDates[i][j] + "</font></html>");
				}
				//当天日期前面加*
				JLabel todayMark = new JLabel("<html><font color=green>*</html>");
				dateButs[i][j].removeAll();
				if ((this.calMonth == today.get(2)) && (this.calYear == today.get(1))
						&& (this.calDates[i][j] == today.get(5))) {
					dateButs[i][j].add(todayMark);
					dateButs[i][j].setToolTipText("Today");
				}
				//为0的日期不显示
				if (this.calDates[i][j] == 0)
					dateButs[i][j].setVisible(false);
				else
					dateButs[i][j].setVisible(true);
			}
	}
	
	/**
	 * read memo data and display the data into the JtextArea
	 */
	public void readMemo() {
		try {
                        //根据日期读取相应的备忘录数据
			File f = new File("MemoData/" + this.calYear + ((this.calMonth + 1 < 10) ? "0" : "") + (this.calMonth + 1)
					+ ((this.calDayOfMon < 10) ? "0" : "") + this.calDayOfMon + ".txt");
                        
                        //文件存在，读取数据并显示在memoArea文本区域
			if (f.exists()) {
                                String fileName = this.calYear + ((this.calMonth + 1 < 10) ? "0" : "") + (this.calMonth + 1)
								+ ((this.calDayOfMon < 10) ? "0" : "") + this.calDayOfMon + ".txt";
                                //文件信息显示在右上角
                                fileInfoLabel.setText(fileName);
                                //读取文件
				BufferedReader in = new BufferedReader(new FileReader("MemoData/" + fileName));
				String memoAreaText = new String();
				while (true) {
					String tempStr = in.readLine();
					if (tempStr == null)
						break;
					memoAreaText = memoAreaText + tempStr + System.getProperty("line.separator");
				}
				memoArea.setText(memoAreaText);
				in.close();
				return;
			}
                        fileInfoLabel.setText("");
			memoArea.setText("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
        
        /**
         * 修改月份，用于切换日历按钮
         * @param mon 
         */
        public void moveMonth(int mon, JButton[][] dateButs ) {
            this.calMonth += mon;
            if (this.calMonth > 11) {
                while(this.calMonth > 11) {
                    ++this.calYear;
                    this.calMonth -= 12;
                }
            } else if (this.calMonth < 0) {
                while(this.calMonth < 0) {
                    --this.calYear;
                    this.calMonth += 12;
                }
            }
            
            GregorianCalendar cal = new GregorianCalendar(this.calYear, this.calMonth, this.calDayOfMon);
            makeCalData(cal);
            showCal(dateButs);
            dateLabel.setText("<html><table width=100><tr><th><font size=5>" + ((calMonth + 1 < 10) ? "&nbsp;" : "")
						+ (calMonth + 1) + " / " +  calYear + "</th></tr></table></html>");
        }
	
	/**
	 * 判断是否为闰年，闰年为1，平年为0
	 * @param year
	 * @return
	 */
	private int leapCheck(int year) {
		if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
			return 1;
		return 0;
	}

	public int getCalYear() {
		return calYear;
	}

	public void setCalYear(int calYear) {
		this.calYear = calYear;
	}

	public int getCalMonth() {
		return calMonth;
	}

	public void setCalMonth(int calMonth) {
		this.calMonth = calMonth;
	}

	public int getCalDayOfMon() {
		return calDayOfMon;
	}

	public void setCalDayOfMon(int calDayOfMon) {
		this.calDayOfMon = calDayOfMon;
	}

	public int getCalLastDate() {
		return calLastDate;
	}

	public void setCalLastDate(int calLastDate) {
		this.calLastDate = calLastDate;
	}

    void setDateLabel(JLabel dateLabel) {
        this.dateLabel = dateLabel;
    }
    
    void setPointLabel(JLabel pointLabel) {
        this.pointLabel = pointLabel;
    }
    
    void setMemoArea(JTextArea memoArea) {
        this.memoArea = memoArea;
    }
    void setFileInfoLabel(JLabel fileInfoLabel) {
        this.fileInfoLabel = fileInfoLabel;
    }

}
