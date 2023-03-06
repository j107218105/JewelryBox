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
			<TITLE>後臺 &#187; 權限 &#187; <xsl:value-of select="@title" disable-output-escaping="yes"/></TITLE>
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
					<xsl:apply-templates select="table"/>
				</DIV>
			</ARTICLE>
		</BODY>
	</xsl:template>

	<xsl:template match="table">
		<TABLE class="list">
			<THEAD>
				<TR>
					<TH>索引</TH>
					<TH>手風琴</TH>
					<TH>頁面標題</TH>
					<TH>路徑</TH>
					<TH>方式</TH>
					<TH>描述</TH>
					<TH>允許</TH>
				</TR>
			</THEAD>
			<TBODY>
				<xsl:for-each select="row">
					<TR>
						<xsl:if test="position()mod'2'='0'">
							<xsl:attribute name="class">odd</xsl:attribute>
						</xsl:if>
						<TD class="textAlignCenter">
							<xsl:value-of select="id"/>
						</TD>
						<TD>
							<xsl:value-of select="accordion"/>
						</TD>
						<TD class="bitstreamVeraSansMono">
							<xsl:value-of select="title"/>
						</TD>
						<TD class="bitstreamVeraSansMono">
							<xsl:value-of select="uri"/>
						</TD>
						<TD>
							<xsl:value-of select="method"/>
						</TD>
						<TD>
							<xsl:value-of select="description"/>
						</TD>
						<TD class="textAlignCenter">
							<A class="ajax fontAwesome" href="./{agent}/mapping/{id}.json">
								<xsl:attribute name="title">
									<xsl:choose>
										<xsl:when test="enabled='true'">有權限；點擊一下即可撤除權限！</xsl:when>
										<xsl:otherwise>無權限；點擊一下即可賦予權限！</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<xsl:choose>
									<xsl:when test="enabled='true'">&#xF00C;</xsl:when>
									<xsl:otherwise>&#xF05E;</xsl:otherwise>
								</xsl:choose>
							</A>
						</TD>
					</TR>
				</xsl:for-each>
			</TBODY>
		</TABLE>
	</xsl:template>

</xsl:stylesheet>