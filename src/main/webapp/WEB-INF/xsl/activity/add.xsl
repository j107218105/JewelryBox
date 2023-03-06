<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.1">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		indent="no"
		media-type="text/html"
	/>

	<xsl:import href="/common.xsl"/>

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
		<HTML dir="ltr" lang="zh-TW">
			<xsl:apply-templates/>
		</HTML>
	</xsl:template>

	<xsl:template match="document">
		<HEAD>
			<META charset="UTF-8"/>
			<META content="width=device-width,initial-scale=1.0" name="viewport"/>
			<LINK href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/vader/jquery-ui.min.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/jquery-ui-timepicker-addon.min.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/default.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/manager.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/i18n/jquery-ui-i18n.min.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/jquery-ui-timepicker-addon.min.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/manager.js"/>
			<TITLE>寶盒計畫 &#187; <xsl:value-of select="@title"/></TITLE>
		</HEAD>
		<BODY>
			<HEADER>
				<DIV class="cF contentWrapper">
					<DIV class="fL">
						<A href="{@contextPath}/">寶盒計畫</A>
						<SPAN>&#187;</SPAN>
						<A>
							<xsl:value-of select="@title"/>
						</A>
					</DIV>
					<DIV class="fR">
						<A>
							<xsl:value-of select="@name"/>
						</A>
						<SPAN>&#187;</SPAN>
						<A href="{@contextPath}/bye.asp">登出</A>
					</DIV>
				</DIV>
			</HEADER>
			<SECTION/>
			<ARTICLE class="contentWrapper">
				<NAV>
					<UL>
						<LI>
							<A>會員</A>
							<UL>
								<LI>
									<A href="{@contextPath}/xxxxxx">待確認</A>
								</LI>
								<LI>
									<A href="{@contextPath}/applicant.asp">待撥款</A>
								</LI>
								<LI>
									<A href="{@contextPath}/blacklist.asp">黑名單</A>
								</LI>
								<LI>
									<A href="{@contextPath}/denied.asp">封鎖</A>
								</LI>
							</UL>
						</LI>
						<LI class="toggled">
							<A>活動</A>
							<UL>
								<LI>
									<A href="{@contextPath}/activity/available.asp">未開始的活動</A>
								</LI>
								<LI>
									<A href="{@contextPath}/activity/underway.asp">進行中的活動</A>
								</LI>
								<LI>
									<A href="{@contextPath}/activity/finished.asp">已結束的活動</A>
								</LI>
								<LI>
									<A>新增活動</A>
								</LI>
							</UL>
						</LI>
					</UL>
				</NAV>
				<DIV id="contentWrapper">
					<xsl:apply-templates select="form"/>
				</DIV>
			</ARTICLE>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<FORM action="{@action}" method="post">
			<FIELDSET>
				<LEGEND>
					<xsl:value-of select="@title"/>
				</LEGEND>
				<TABLE class="fieldset">
					<TBODY>
						<TR>
							<TH class="must">
								<LABEL for="name">活動名稱</LABEL>
							</TH>
							<TD>
								<INPUT id="name" name="name" type="text" value="{name}"/>
							</TD>
							<TD class="note">必填。</TD>
						</TR>
						<TR>
							<TH class="must">
								<LABEL for="headcount">人數</LABEL>
							</TH>
							<TD>
								<INPUT id="headcount" name="headcount" type="text" value="{headcount}"/>
							</TD>
							<TD class="note">必填；此活動的人數上限。</TD>
						</TR>
						<TR>
							<TH class="must">
								<LABEL for="score">點數</LABEL>
							</TH>
							<TD>
								<INPUT id="score" name="score" type="text" value="{score}"/>
							</TD>
							<TD class="note">必填；完成的會員可獲得的點數。</TD>
						</TR>
						<TR>
							<TH class="must">
								<LABEL for="since">開始時戳</LABEL>
							</TH>
							<TD>
								<INPUT class="dtP" id="since" name="since" readonly="" type="text" value="{since}"/>
							</TD>
							<TD class="note">必填；不得遲於開始日期。</TD>
						</TR>
						<TR>
							<TH class="must">
								<LABEL for="until">結束時戳</LABEL>
							</TH>
							<TD>
								<INPUT class="dtP" id="until" name="until" readonly="" type="text" value="{until}"/>
							</TD>
							<TD class="note">必填；不得早於開始日期。</TD>
						</TR>
						<TR>
							<TH>
								<LABEL for="age">年齡需求</LABEL>
							</TH>
							<TD>
								<INPUT id="age" name="age" type="text" value="{age}"/>
							</TD>
							<TD class="note">非必填；以開始日期起計。</TD>
						</TR>
						<TR>
							<TH>性別需求</TH>
							<TD>
								<LABEL>
									<INPUT name="gender" type="radio" value="false"/>
									<SPAN>女性</SPAN>
								</LABEL>
								<LABEL>
									<INPUT name="gender" type="radio" value="true"/>
									<SPAN>男性</SPAN>
								</LABEL>
							</TD>
							<TD class="note">非必選。</TD>
						</TR>
						<TR>
							<TH>
								<LABEL for="occupation">職業需求</LABEL>
							</TH>
							<TD>
								<SELECT id="occupation" name="occupation">
									<OPTION value="">(職業)</OPTION>
									<xsl:apply-templates select="occupation/*"/>
								</SELECT>
							</TD>
							<TD class="note">非必選。</TD>
						</TR>
					</TBODY>
				</TABLE>
				<DIV class="cF">
					<INPUT class="fL" type="reset" value="取消"/>
					<INPUT class="fR" type="submit" value="確定"/>
				</DIV>
			</FIELDSET>
		</FORM>
	</xsl:template>

</xsl:stylesheet>