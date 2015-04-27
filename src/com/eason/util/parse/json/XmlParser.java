package com.eason.util.parse.json;

import com.lenovo.nova.util.debug.slog;
import org.xmlpull.v1.XmlPullParser;

public class XmlParser {
	private static final String TAG = "XmlParser";
	private XmlPullParser mParser;
	private boolean openDebug = true;

	public XmlParser(XmlPullParser parser) {
		mParser = parser;
	}

	public void parser() {
		int event;
		try {
			event = mParser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
					p("parse xml start document");
					break;
				case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
					{
						String name = mParser.getName();
						p("parse xml start tag  " + name);
						p(debugAttributeValue(mParser));
						onExecuteStartTagEvent(mParser, name);
					}
					break;
				case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
					{
						String name = mParser.getName();
						p("parse xml end tag " + name);
						onExecuteEndTagEvent(mParser, name);
					}
					break;
				}
				event = mParser.next();// 进入下一个元素并触发相应事件
			}// end while
			p("parse xml end document");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onExecuteEndTagEvent(XmlPullParser parser, String name) {
		// TODO Auto-generated method stub

	}

	protected void onExecuteStartTagEvent(XmlPullParser parser, String tag) {

	}

	public void setDebugInfor(boolean openDebug) {
		this.openDebug = openDebug;
	}

	public String debugAttributeValue(XmlPullParser xrp) {
		if (openDebug) {
			int count = xrp.getAttributeCount();
			StringBuilder sb = new StringBuilder();
			sb.append("Tag=" + xrp.getName() + "  ");
			for (int i = 0; i < count; i++) {
				sb.append(xrp.getAttributeName(i) + "=" + xrp.getAttributeValue(i));
				sb.append(" ; ");
			}
			sb.append(" LineNumber:" + xrp.getLineNumber());
		
			return sb.toString();
		}else{
			return "need open debug switch";
		}

	}

	private void p(String str) {
		slog.p(TAG, str);
	}

	private void e(String str) {
		slog.e(TAG, str);
	}
}
