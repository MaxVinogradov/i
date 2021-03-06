var express = require('express')
  , router = express.Router()
  , form = require('./form.controller')
  , auth = require('../../auth/auth.service.js')
  , region = require('../../components/region')
  , endpoint = require('../../components/endpoint');

router.get('/', endpoint.assertQueryParams('nID_Server', 'sID_BP_Versioned'), form.index);
router.post('/', form.submit);
router.get('/sign', form.signForm);
router.use('/sign/callback', form.signFormCallback);
router.get('/sign/check', form.signCheck);
router.post('/save', form.saveForm);
router.get('/load', form.loadForm);
router.post('/scansUpload', form.scanUpload);

module.exports = router;
