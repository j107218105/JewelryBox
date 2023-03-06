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
			<LINK href="{@contextPath}/STYLE/default.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="{@contextPath}/STYLE/manager.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"/>
			<SCRIPT src="{@contextPath}/SCRIPT/manager.js"/>
			<TITLE>寶盒計畫 &#187; <xsl:value-of select="@title"/></TITLE>
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
			<SECTION>
				<!--<xsl:apply-templates select="search"/>-->
			</SECTION>
			<ARTICLE class="contentWrapper">
				<NAV>
					<xsl:apply-templates select="navigator">
						<xsl:with-param name="contextPath" select="@contextPath"/>
					</xsl:apply-templates>
				</NAV>
				<DIV id="contentWrapper">
					<xsl:apply-templates select="table"/>
				</DIV>
			</ARTICLE>
		</BODY>
	</xsl:template>

	<xsl:template match="table">
		<!--
		<TABLE class="list">
			<THEAD>
				<TR>
					<TH>狀態</TH>
					<TH>會員帳號</TH>
					<TH>年齡</TH>
					<TH>性別</TH>
					<TH>全名</TH>
					<TH>電子郵件</TH>
					<TH>手機</TH>
					<TH>職業</TH>
					<TH>金融機構</TH>
					<TH>金融戶名</TH>
					<TH>金融帳號</TH>
				</TR>
			</THEAD>
			<TBODY>
				<xsl:for-each select="row">
					<TR>
						<xsl:if test="position()mod'2'='0'">
							<xsl:attribute name="class">odd</xsl:attribute>
						</xsl:if>
						<TD>
							<xsl:if test="denied='true'">
								<A class="fontAwesome">&#xF017;</A>
							</xsl:if>
						</TD>
						<TD>
							<xsl:choose>
								<xsl:when test="facebookId">
									<SPAN class="facebook fontAwesome">&#xF082;</SPAN>
									<B>&#160;</B>
									<SPAN class="bitstreamVeraSansMono socialId">
										<xsl:value-of select="facebookId"/>
									</SPAN>
								</xsl:when>
								<xsl:when test="googleId">
									<SPAN class="fontAwesome google">&#xF0D4;</SPAN>
									<B>&#160;</B>
									<SPAN class="bitstreamVeraSansMono socialId">
										<xsl:value-of select="googleId"/>
									</SPAN>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="id"/>
								</xsl:otherwise>
							</xsl:choose>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:value-of select="age"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:if test="gender='false'">
								<SPAN class="fontAwesome">&#xF182;</SPAN>
							</xsl:if>
							<xsl:if test="gender='true'">
								<SPAN class="fontAwesome">&#xF183;</SPAN>
							</xsl:if>
						</TD>
						<TD>
							<xsl:value-of select="name"/>
						</TD>
						<TD>
							<xsl:value-of select="email"/>
						</TD>
						<TD class="bitstreamVeraSansMono">
							<xsl:value-of select="cellular"/>
						</TD>
						<TD>
							<xsl:value-of select="occupation"/>
						</TD>
						<TD>
							<xsl:value-of select="financialInstitution"/>
						</TD>
						<TD>
							<xsl:value-of select="financialAccountHolder"/>
						</TD>
						<TD class="bitstreamVeraSansMono">
							<xsl:value-of select="financialAccountNumber"/>
						</TD>
					</TR>
				</xsl:for-each>
			</TBODY>
		</TABLE>
		-->
	</xsl:template>

</xsl:stylesheet>