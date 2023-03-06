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
			<TITLE>後臺 &#187; <xsl:value-of select="@title" disable-output-escaping="yes"/> &#187; 活動一覽</TITLE>
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
		<xsl:param name="contextPath" select="contextPath"/>
		<TABLE class="list">
			<THEAD>
				<TR>
					<TH>索引</TH>
					<TH>活動名稱</TH>
					<TH>人數</TH>
					<TH>點數</TH>
					<TH>開始時戳</TH>
					<TH>結束時戳</TH>
					<TH>年齡需求</TH>
					<TH>性別需求</TH>
					<TH>職業需求</TH>
					<TH>行政區需求</TH>
					<TH>參與情況</TH>
					<TH>回覆時戳</TH>
					<TH>完成情況</TH>
					<TH>審核情況</TH>
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
							<xsl:value-of select="name"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:value-of select="headcount"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:value-of select="score"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:value-of select="since"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:value-of select="until"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:value-of select="age"/>
						</TD>
						<TD class="bitstreamVeraSansMono textAlignCenter">
							<xsl:if test="gender='false'">
								<SPAN class="fontAwesome" style="color:#FF8040">&#xF182;</SPAN>
							</xsl:if>
							<xsl:if test="gender='true'">
								<SPAN class="fontAwesome" style="color:#4080FF">&#xF183;</SPAN>
							</xsl:if>
						</TD>
						<TD>
							<xsl:value-of select="occupation"/>
						</TD>
						<TD>
							<xsl:value-of select="district"/>
						</TD>
						<TD class="textAlignCenter">
							<xsl:choose>
								<xsl:when test="replied='false'">
									<SPAN class="fontAwesome">&#xF00D;</SPAN>
									<B>&#160;</B>
									<SPAN>棄權</SPAN>
								</xsl:when>
								<xsl:when test="replied='true'">
									<SPAN class="fontAwesome">&#xF00C;</SPAN>
									<B>&#160;</B>
									<SPAN>參與</SPAN>
								</xsl:when>
								<xsl:otherwise>
									<SPAN class="fontAwesome">&#xF128;</SPAN>
									<B>&#160;</B>
									<SPAN>未回覆</SPAN>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="replied"/>
						</TD>
						<TD>
							<xsl:value-of select="repliedBy"/>
						</TD>
						<TD class="textAlignCenter">
							<xsl:choose>
								<xsl:when test="proof='false'">
									<SPAN class="fontAwesome">&#xF00D;</SPAN>
									<B>&#160;</B>
									<SPAN>未完成</SPAN>
								</xsl:when>
								<xsl:otherwise>
									<SPAN class="fontAwesome">&#xF00C;</SPAN>
									<B>&#160;</B>
									<SPAN>已完成</SPAN>
									<xsl:if test="provedBy">
										<SPAN>於</SPAN>
										<xsl:value-of select="provedBy"/>
									</xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</TD>
						<TD>
							<xsl:value-of select="agent"/>
						</TD>
						<!--
						<TD class="textAlignCenter">
							<A class="ajax fontAwesome" href="{id}.json">
								<xsl:attribute name="title">
									<xsl:choose>
										<xsl:when test="denied='true'">封鎖中；點擊一下即可取消封鎖！</xsl:when>
										<xsl:otherwise>開放中；點擊一下即可封鎖會員！</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<xsl:choose>
									<xsl:when test="denied='true'">&#xF05E;</xsl:when>
									<xsl:otherwise>&#xF00C;</xsl:otherwise>
								</xsl:choose>
							</A>
						</TD>
						<TD class="textAlignCenter">
							<A class="fontAwesome" href="{id}/activity/">&#xF00B;</A>
						</TD>
						-->
					</TR>
				</xsl:for-each>
			</TBODY>
		</TABLE>
	</xsl:template>

</xsl:stylesheet>