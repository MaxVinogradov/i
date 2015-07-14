package org.wf.dp.dniprorada.engine.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.wf.dp.dniprorada.base.dao.AccessDataDao;
import org.wf.dp.dniprorada.constant.Currency;
import org.wf.dp.dniprorada.constant.Language;
import org.wf.dp.dniprorada.liqPay.LiqBuy;
import org.wf.dp.dniprorada.util.GeneralConfig;

public abstract class BaseMailTaskWithAttachment implements JavaDelegate {

    private static final String TAG_PAYMENT_BUTTON_LIQPAY = "[paymentButton_LiqPay]";
    
    private static final String LIQPAY_CALLBACK_URL = "https://test.region.igov.org.ua/wf-region/service/setPaymentStatus_TaskActiviti?sID_Order={0}&sID_PaymentSystem=\"Liqpay\"&sData = \"\"";
    private static final String TAG_nID_SUBJECT = "[nID_Subject]";
    private static final String TAG_sACCESS_KEY = "[sAccessKey]";
    private static final String TAG_sURL_SERVICE_MESSAGE = "[sURL_ServiceMessage]";
    private static final String URL_SERVICE_MESSAGE = "https://test.igov.org.ua/wf-central/service/messages/setMessage";

    @Autowired
    GeneralConfig generalConfig;

    @Autowired
    AccessDataDao accessDataDao;
    
    protected Expression sID_Merchant;
    protected Expression sSum;
    protected Expression sID_Currency;
    protected Expression sLanguage;
    protected Expression sDescription;
    protected Expression nID_Subject;
    
    protected String replaceTags(String textStr, DelegateExecution execution) throws Exception {
        if (textStr == null) {
            return null;
        }
        String textWithoutTags = textStr;
        if (textStr.contains(TAG_PAYMENT_BUTTON_LIQPAY)) {
            String sID_Merchant = getStringFromFieldExpression(this.sID_Merchant, execution);
            String sSum = getStringFromFieldExpression(this.sSum, execution);
            Currency sID_Currency = Currency.valueOf(getStringFromFieldExpression(this.sID_Currency, execution));
            Language sLanguage = LiqBuy.DEFAULT_LANG;
            String sDescription = getStringFromFieldExpression(this.sDescription, execution);
            String sID_Order = "TaskActiviti_" + execution.getId();  // TODO: not sure about id
            String sURL_CallbackStatusNew = StringUtils.replace(LIQPAY_CALLBACK_URL, "{0}", sID_Order); 
            String sURL_CallbackPaySuccess = null;
            Long nID_Subject = getLongFromFieldExpression(this.nID_Subject, execution);
            boolean bTest = generalConfig.bTest();
            String htmlButton = new LiqBuy().getPayButtonHTML_LiqPay(sID_Merchant, sSum, sID_Currency, sLanguage, sDescription, sID_Order, sURL_CallbackStatusNew, sURL_CallbackPaySuccess, nID_Subject, bTest);
            textWithoutTags = StringUtils.replace(textStr, TAG_PAYMENT_BUTTON_LIQPAY, htmlButton);
        }

        if (textWithoutTags.contains(TAG_nID_SUBJECT)) {
            textWithoutTags = textWithoutTags.replaceAll(TAG_nID_SUBJECT, "" + nID_Subject);
        }
        if (textWithoutTags.contains(TAG_sACCESS_KEY)) {
            textWithoutTags = textWithoutTags.replaceAll(TAG_sACCESS_KEY, accessDataDao.setAccessData("" + nID_Subject));
        }
        if (textWithoutTags.contains(TAG_sURL_SERVICE_MESSAGE)) {
            textWithoutTags = textWithoutTags.replaceAll(TAG_sURL_SERVICE_MESSAGE, URL_SERVICE_MESSAGE);
        }
        return textWithoutTags;
    }

    protected String getStringFromFieldExpression(Expression expression, DelegateExecution execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
    
    protected Long getLongFromFieldExpression(Expression expression, DelegateExecution execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            if (value != null) {
                return Long.valueOf(value.toString());
            }
        }
        return null;
    }

}
