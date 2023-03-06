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
				<!--<xsl:apply-templates select="search"/>-->
				<FORM id="pagination" class="contentWrapper" action="{concat(@contextPath,search/@action)}" method="get">
					<INPUT name="q" placeholder="搜尋" type="text" value="{search/@query}"/>
					<xsl:if test="search/@previous">
						<xsl:if test="search/@first">
							<A class="button fontAwesome paginate" title="第一頁" tabindex="{search/@first}">&#xF049;</A>
						</xsl:if>
						<A class="button fontAwesome paginate" title="上一頁" tabindex="{search/@previous}">&#xF04A;</A>
					</xsl:if>
					<SPAN style="margin:0 3px">
						<LABEL>
							<SPAN>每頁</SPAN>
							<INPUT maxlength="2" name="s" type="text" value="{search/@size}"/>
							<SPAN>筆</SPAN>
						</LABEL>
						<SPAN>：</SPAN>
						<LABEL>
							<SPAN>第</SPAN>
							<SELECT name="p">
								<xsl:apply-templates select="search/*"/>
							</SELECT>
							<SPAN>&#47;</SPAN>
							<SPAN>
								<xsl:value-of select="search/@totalPages"/>
							</SPAN>
							<SPAN>頁</SPAN>
						</LABEL>
						<SPAN>&#40;共</SPAN>
						<SPAN>
							<xsl:value-of select="search/@totalElements"/>
						</SPAN>
						<SPAN>筆&#41;</SPAN>
					</SPAN>
					<xsl:if test="search/@next">
						<A class="button fontAwesome paginate" title="下一頁" tabindex="{search/@next}">&#xF04E;</A>
						<xsl:if test="search/@last">
							<A class="button fontAwesome paginate" title="最後頁" tabindex="{search/@last}">&#xF050;</A>
						</xsl:if>
					</xsl:if>
					<INPUT style="display:none" type="submit"/>
				</FORM>
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
					<TH>會員帳號</TH>
					<TH>年齡</TH>
					<TH>性別</TH>
					<TH>真實姓名</TH>
					<TH>電子郵件</TH>
					<TH>狀態</TH>
					<TH>手機</TH>
					<TH>職業</TH>
					<TH>金融機構</TH>
					<TH>金融戶名</TH>
					<TH>金融帳號</TH>
					<TH>行政區</TH>
					<TH>管理者</TH>
					<TH>活動一覽</TH>
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
								<SPAN class="fontAwesome" style="color:#FF8040">&#xF182;</SPAN>
							</xsl:if>
							<xsl:if test="gender='true'">
								<SPAN class="fontAwesome" style="color:#4080FF">&#xF183;</SPAN>
							</xsl:if>
						</TD>
						<TD>
							<xsl:value-of select="name"/>
						</TD>
						<TD class="bitstreamVeraSansMono">
							<xsl:value-of select="email"/>
						</TD>
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
						<TD>
							<xsl:value-of select="district"/>
						</TD>
						<TD class="textAlignCenter">
							<A class="ajax fontAwesome" href="{id}/agent.json">
								<xsl:attribute name="title">
									<xsl:choose>
										<xsl:when test="agent='true'">管理者；點擊一下即可停權！</xsl:when>
										<xsl:otherwise>一般會員；點擊一下即可授權！</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<xsl:choose>
									<xsl:when test="agent='true'">&#xF0F0;</xsl:when>
									<xsl:otherwise>&#xF0C0;</xsl:otherwise>
								</xsl:choose>
							</A>
						</TD>
						<TD class="textAlignCenter">
							<A class="fontAwesome" href="{id}/activity/">&#xF00B;</A>
						</TD>
					</TR>
				</xsl:for-each>
			</TBODY>
		</TABLE>
	</xsl:template>

</xsl:stylesheet>