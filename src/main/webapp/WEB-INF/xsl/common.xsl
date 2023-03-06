<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		indent="no"
		media-type="text/html"
	/>

	<xsl:template match="optgroup">
		<OPTGROUP label="{@label}">
			<xsl:apply-templates/>
		</OPTGROUP>
	</xsl:template>

	<xsl:template match="option">
		<OPTION value="{@value}">
			<xsl:if test="@selected">
				<xsl:attribute name="selected"/>
			</xsl:if>
			<xsl:value-of select="." disable-output-escaping="yes"/>
		</OPTION>
	</xsl:template>

	<xsl:template match="navigator">
		<xsl:param name="contextPath"/>
		<xsl:for-each select="accordion">
			<xsl:sort select="@ordinal" data-type="number"/>
			<UL>
				<LI>
					<xsl:if test="@toggled='true'">
						<xsl:attribute name="class">toggled</xsl:attribute>
					</xsl:if>
					<A>
						<xsl:value-of select="@name"/>
					</A>
					<UL>
						<xsl:for-each select="mapping">
							<xsl:sort select="@ordinal" data-type="number"/>
							<LI>
								<A>
									<xsl:choose>
										<xsl:when test="@uri">
											<xsl:attribute name="href">
												<xsl:value-of select="concat($contextPath,@uri)"/>
											</xsl:attribute>
										</xsl:when>
										<xsl:otherwise>
											<xsl:attribute name="class">current</xsl:attribute>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:value-of select="." disable-output-escaping="yes"/>
								</A>
							</LI>
						</xsl:for-each>
					</UL>
				</LI>
			</UL>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="search">
		<FORM id="pagination" class="contentWrapper" action="{concat(@contextPath,@action)}" method="get">
			<xsl:if test="@previous">
				<xsl:if test="@first">
					<A class="button fontAwesome paginate" title="第一頁" tabindex="{@first}">&#xF049;</A>
				</xsl:if>
				<A class="button fontAwesome paginate" title="上一頁" tabindex="{@previous}">&#xF04A;</A>
			</xsl:if>
			<SPAN style="margin:0 3px">
				<LABEL>
					<SPAN>每頁</SPAN>
					<INPUT maxlength="2" name="s" type="text" value="{@size}"/>
					<SPAN>筆</SPAN>
				</LABEL>
				<SPAN>：</SPAN>
				<LABEL>
					<SPAN>第</SPAN>
					<SELECT name="p">
						<xsl:apply-templates select="*"/>
					</SELECT>
					<SPAN>&#47;</SPAN>
					<SPAN>
						<xsl:value-of select="@totalPages"/>
					</SPAN>
					<SPAN>頁</SPAN>
				</LABEL>
				<SPAN>&#40;共</SPAN>
				<SPAN>
					<xsl:value-of select="@totalElements"/>
				</SPAN>
				<SPAN>筆&#41;</SPAN>
			</SPAN>
			<xsl:if test="@next">
				<A class="button fontAwesome paginate" title="下一頁" tabindex="{@next}">&#xF04E;</A>
				<xsl:if test="@last">
					<A class="button fontAwesome paginate" title="最後頁" tabindex="{@last}">&#xF050;</A>
				</xsl:if>
			</xsl:if>
		</FORM>
	</xsl:template>

</xsl:stylesheet>