#!/usr/bin/env python
#-*- coding:utf-8 -*-

"""
desc:     Log tool
date:     2016-08-25                                                         
"""
import logging
import os

class Logger(object):
    """
    封装好的Logger工具
    """

    _logger = ''

    @classmethod
    def init_log(cls):
        """
        initial
        """
        log_file = './log/xxx-srv.log'
        logging.addLevelName(10, "DEBUG: ")
        logging.addLevelName(20, "NOTICE:")
        logging.addLevelName(30, "WARN:  ")
        logging.addLevelName(40, "FATAL: ")
        logging.addLevelName(50, "FATAL: ")
        cls._logger = logging.getLogger('xxx-log')
        cls._logger.setLevel(logging.DEBUG)
        if not cls._logger.handlers:
            file_handler = logging.FileHandler(log_file)
            formatter = logging.Formatter('%(levelname)s %(asctime)s [pid:%(process)s] %(message)s', 
                                          '%Y-%m-%d %H:%M:%S')
            file_handler.setFormatter(formatter)
            cls._logger.addHandler(file_handler)
        #console = logging.StreamHandler()
        #console.setLevel(logging.DEBUG)
        #formatter = logging.Formatter("%(levelname)s [pid:%(process)s] %(message)s")
        #console.setFormatter(formatter)
        #cls._logger.addHandler(console)
    
    @classmethod
    def debug(cls, msg=""):
        """
        output DEBUG level LOG
        """
        cls._logger.debug(str(msg))
    
    @classmethod
    def info(cls, msg=""):
        """
        output INFO level LOG
        """
        cls._logger.info(str(msg))

    @classmethod
    def warning(cls, msg=""):
        """
        output WARN level LOG
        """
        cls._logger.warning(str(msg))

    @classmethod
    def exception(cls, msg=""):
        """
        output Exception stack LOG
        """
        cls._logger.exception(str(msg))

    @classmethod
    def error(cls, msg=""):
        """
        output ERROR level LOG
        """
        cls._logger.error(str(msg))

    @classmethod
    def critical(cls, msg=""):
        """
        output FATAL level LOG
        """
        cls._logger.critical(str(msg))
    

if __name__ == "__main__":
    Logger.init_log()
    Logger.info("info....")
    Logger.warning("warning....")
    Logger.critical("critical....")
