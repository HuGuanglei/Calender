/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

/**
 *
 * @author emma
 */
public class CalButtonAciton implements ActionListener {
    CalendarMemoApp memoCalendar;
    CalButtonAciton(CalendarMemoApp memoCalendar){
            this.memoCalendar = memoCalendar;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int k = 0;
        int l = 0;
        //get the click date
        for (int i = 0; i < 6; ++i) {
                for (int j = 0; j < 7; ++j) {
                        if (e.getSource() == memoCalendar.dateButs[i][j]) {
                                k = i;
                                l = j;
                        }
                }
        }

        if ((k != 0) || (l != 0))
                memoCalendar.cm.calDayOfMon = memoCalendar.cm.calDates[k][l];

        //display data
        memoCalendar.cm.pointLabel.setText(memoCalendar.cm.getCalMonth() + 1 + "/" + memoCalendar.cm.getCalDayOfMon() + "/" + memoCalendar.cm.getCalYear());
        //load the content of the mome
        memoCalendar.cm.readMemo();
    }
    
}
