<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		doctype-system="about:blank"
		indent="no"
		media-type="text/html"
	/>

	<xsl:import href="/common.xsl"/>

	<xsl:template match="/">
		<HTML dir="ltr" lang="zh-TW">
			<xsl:apply-templates/>
		</HTML>
	</xsl:template>

	<xsl:template match="document">
		<HEAD>
			<META charset="UTF-8"/>
			<META content="width=device-width,initial-scale=1.0" name="viewport"/>
			<LINK href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/themes/vader/jquery-ui.min.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.4.5/jquery-ui-timepicker-addon.min.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/default.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/manager.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="form.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/jquery-ui.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.3/i18n/jquery-ui-i18n.min.js"/>
			<SCRIPT src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.4.5/jquery-ui-timepicker-addon.min.js"/>
			<SCRIPT src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.4.5/i18n/jquery-ui-timepicker-addon-i18n.min.js"/>
			<SCRIPT src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.4.5/i18n/jquery-ui-timepicker-zh-TW.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/manager.js"/>
			<SCRIPT src="form.js"/>
			<TITLE>後臺 &#187; <xsl:value-of select="@title" disable-output-escaping="yes"/></TITLE>
		</HEAD>
		<xsl:comment>
			<xsl:value-of select="system-property('xsl:version')"/>
		</xsl:comment>
		<BODY>
			<HEADER>
				<DIV class="cF contentWrapper">
					<DIV class="fL">
						<A href="{@contextPath}/">寶盒計畫</A>
						<SPAN>&#187;</SPAN>
						<A>
							<xsl:value-of select="@title" disable-output-escaping="yes"/>
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
			<SECTION>
				<xsl:apply-templates select="search"/>
			</SECTION>
			<ARTICLE class="contentWrapper">
				<NAV>
					<xsl:apply-templates select="navigator">
						<xsl:with-param name="contextPath" select="@contextPath"/>
					</xsl:apply-templates>
				</NAV>
				<DIV id="contentWrapper">
					<xsl:apply-templates select="form"/>
				</DIV>
			</ARTICLE>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<FORM action="{@action}" method="POST">
			<FIELDSET>
				<TABLE class="fieldset">
					<CAPTION>
						<xsl:value-of select="errorMessage"/>
					</CAPTION>
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
								<INPUT id="headcount" maxlength="9" name="headcount" type="text" value="{headcount}"/>
							</TD>
							<TD class="note">必填；此活動的人數上限。</TD>
						</TR>
						<TR>
							<TH class="must">
								<LABEL for="score">價值點數</LABEL>
							</TH>
							<TD>
								<INPUT id="score" maxlength="4" name="score" type="text" value="{score}"/>
							</TD>
							<TD class="note">必填；會員完成活動可獲得的點數。</TD>
						</TR>
						<TR>
							<TH class="must">
								<LABEL for="since">開始時戳</LABEL>
							</TH>
							<TD>
								<INPUT class="dtP" id="since" name="since" readonly="" type="text" value="{since}"/>
							</TD>
							<TD class="note">必填；不得遲於結束日期。</TD>
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
									<INPUT name="gender" type="radio" value="false">
										<xsl:if test="gender='false'">
											<xsl:attribute name="checked"/>
										</xsl:if>
									</INPUT>
									<SPAN>女性</SPAN>
								</LABEL>
								<LABEL>
									<INPUT name="gender" type="radio" value="true">
										<xsl:if test="gender='true'">
											<xsl:attribute name="checked"/>
										</xsl:if>
									</INPUT>
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
						<TR>
							<TH>
								<LABEL for="district">行政區需求</LABEL>
							</TH>
							<TD>
								<SELECT id="district" name="district">
									<OPTION value="">(行政區)</OPTION>
									<xsl:apply-templates select="district/*"/>
								</SELECT>
							</TD>
							<TD class="note">非必選。</TD>
						</TR>
					</TBODY>
				</TABLE>
				<P class="cF">
					<INPUT class="fL" type="reset" value="復原"/>
					<INPUT class="fR" type="submit" value="確定"/>
				</P>
				<DIV id="ajaxResult"/>
			</FIELDSET>
		</FORM>
	</xsl:template>

</xsl:stylesheet>