#!/usr/bin/env python
#-*- coding:utf-8 -*-

#file: mon_info.py
#date: 2016-08-26

import time
import json

def main():
    file_mon = '/home/wahaha/monitor_info/monitor.file' #json格式的文本文件
    file_obj = open(file_mon)
    str_info = file_obj.read()
    file_obj.close()

    monitor_info = json.loads(str_info)['serverctrl']
    xx_timestamp = min([mon_info['XX_HEARTBEAT_%s' % x] for x in range(0, 9)])
    yy_timestamp = min([mon_info['YY_HEARTBEAT_%s' % x] for x in range(0, 2)])
    total_qps = monitor_info['server_ctrl']['TOTAL_QPS']
    
    now_timestamp = int(time.time())
    xx_sentry = now_timestamp - XX_timestamp
    xx_sentry = now_timestamp - YY_timestamp

    print 'XX_SENTRY:', xx_sentry/60
    print 'YY_SENTRY:', yy_sentry/60
    print 'TOTAL_QPS:', total_qps
    print 'EOF'

if __name__ == "__main__":
    main()
