from . import app, setup_logging
from . import login
from . import web

from werkzeug.contrib.fixers import ProxyFix
app.wsgi_app = ProxyFix(app.wsgi_app)

import flask
import flask_oauthlib.client
from flask_compress import Compress

compress = Compress()
compress.init_app(app)


setup_logging("api_server.log", app.logger)
setup_logging("oauth.log", flask_oauthlib.client.log)
setup_logging("login.log", login.login_log)
app.register_blueprint(login.oauth_login, url_prefix="/v1/login")
app.register_blueprint(login.oauth_logout, url_prefix="/v1/logout")
app.register_blueprint(web.web_api, url_prefix="/v1/api")

@app.before_request
def force_https():
    if not flask.request.is_secure and flask.request.endpoint and \
            not flask.request.url.startswith('http://localhost') and \
            not app.config["DEBUG"] == True and \
            flask.request.endpoint != 'health_check':
        return flask.redirect(flask.request.url.replace('http://', 'https://'))
