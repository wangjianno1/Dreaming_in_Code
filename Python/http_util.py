#! /usr/bin/python
# -*- coding: utf-8 -*-
"""
file: http_util.py
data: 2016-08-25
"""

import json
import urllib
import urllib2
import httplib
import logging
from urlparse import urlparse

def req_url(url, data=None):
    """request url
    """
    req = urllib2.Request(url)
    # add Accept
    if not req.has_header('Accept'):
        req.add_header('Accept', '*/*')

    default_timeout = 10000000
    ret = True
    code = 0
    page = ''
    try:
        r = urllib2.urlopen(req, data = None, timeout = default_timeout)
        code = r.getcode()
        page = r.read()
    except Exception as ex:
        logging.exception('caught exception.')
        ret = False
    finally:
        return ret, code, page


def req_http(method, url, post_data=None, log_ignore=None, auth=None, timeout=None):
    """http请求接口
       input:
             method, http method
             url, http请求url
             post_data, http请求参数
             log_ignore,
             auth, http认证信息
             timeout, 超时时间
       output:
             ret, True or False, 接口是否调用成功
             status, http返回状态码
             resp, http  response
    """
    log_url = ("url[%s] data[%s]" % (url, json.dumps(post_data))) \
            if post_data else ("url[%s]" % url)
    log_url = log_url.replace(log_ignore, "****") if log_ignore else log_url
    logging.debug('req_http request: {} {}'.format(method, log_url))
    host = urlparse(url).netloc
    path = urlparse(url).path
    if urlparse(url).query:
        path = '%s?%s' % (path, urlparse(url).query)
    
    headers = {'Accept' : '*/*', 'Connection' : 'close', 'Content-Type' : 'application/x-www-form-urlencoded'}
    if auth:
        headers['Authorization'] = auth

    print 'url:', url
    print 'method:', method
    print 'post_data:', post_data
    if post_data:
        post_params = urllib.urlencode(dict([k, unicode(v).encode('utf8')] for k, v in post_data.items()))
    else:
        post_params = None

    if method.upper() == 'GET' and '?' not in url and post_params is not None:
        path += '?%s' % post_params
        post_params = None

    ret = False
    status = 0
    resp = None
    try:
        DEFAULT_TIMEOUT = 30
        u_timeout = timeout or DEFAULT_TIMEOUT
        conn = httplib.HTTPConnection(host, timeout = u_timeout)
        conn.set_debuglevel(0)
        conn.request(method, path, post_params, headers)
        r = conn.getresponse()
        ret = True
        status = r.status
        resp = r.read().rstrip()
    except Exception as ex:
        logging.exception('req url:{} caught exception'.format(url))
    finally:
        if conn:
            conn.close()
        return ret, status, resp

