#! /usr/bin/python
# -*- coding: utf-8 -*-
"""
file: const.py
data: 2016-08-25
"""

class AutoCheckResult(object):
    """自动检查结果
    """
    PASS = 0 #检查通过
    FAIL = 1 #检查失败
    REDO = 2 #重新检查
    ERR = 3  #接口异常, 不重新检查

    names = ['PASS',
             'FAIL',
             'REDO',
             'ERR'
            ]
    name_map = dict(zip(names, range(len(names))))

def get_check_result():
    return AutoCheckResult.FAIL

if __name__ == '__main__':
    check_retcode = get_check_result()
    if check_retcode == AutoCheckResult.PASS:
        print '检查通过!!'
        # 使用AutoCheckResult.names[]来获取可读性好的结果
        print 'check result: {}'.format(AutoCheckResult.names[check_retcode])
    else:
        print '检查异常!!'
        print 'check result: {}'.format(AutoCheckResult.names[check_retcode])
