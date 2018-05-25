[#include "/email/emailLayout.ftl"]

[@emailLayout]

<!-- this is content start -->
 <tr>
     <td align="left" valign="top" bgcolor="#FFFFFF" style="background-color:#FFFFFF;">
         <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>

                 <td align="left" valign="top">
                     <table width="480" border="0" cellspacing="0" cellpadding="0"
                            align="center">
                         <tr>
                             <td align="left" valign="top">
                                 <div style="color:#2D3E51; font-family: Arial, sans-serif; font-size:14px; font-weight: 600; line-height:21px; margin-top: 50px;">
                                     Hi there,
                                 </div>
                                 <br/>
                                 <div style="font-family: Arial, sans-serif; color:#2D3E51; font-size:12px; line-height:21px;">
                                     You asked to reset your password. Click the button below to get started. <br>
                                     <a href="${customerSiteDomain}/reset?encryptId=${encryptId}">Reset Password</a>
                                 </div>
                                 <br/>
                             </td>
                             </td>
                         </tr>

                         <tr>
                             <td align="left" valign="top">
                                 <div style="color:#2D3E51; font-family: Arial, sans-serif; font-size:12px; line-height:21px; width: 480px; margin: 0 auto;">
                                     If clicking the button above doesn't work, then copy the following URL into your browser bar.
                                     <br>
                                     ${customerSiteDomain}/reset?encryptId=${encryptId}
                                 </div>
                                 <br/>

                             </td>
                         </tr>

                         <tr>
                             <td align="left" valign="top">
                                 <div style="color:#2D3E51; font-family: Arial, sans-serif; font-size:12px; line-height:21px; width: 480px; margin: 0 auto;">
                                     If you didn’t ask to reset your password, just let us know.
                                 </div>
                                 <br/>
                             </td>
                         </tr>

                     </table>

                 </td>
             </tr>
         </table>
     </td>
 </tr>

<!-- this is content end -->

[/@emailLayout]
