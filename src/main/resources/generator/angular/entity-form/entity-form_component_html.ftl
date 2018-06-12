
<#setting classic_compatible=true>
<!-- MAIN CONTENT -->
<div id="content">
    <!--<div class="row">-->
    <!--<sa-big-breadcrumbs [breadCrumbs]="breadCrumbs" icon="pencil-square-o"-->
    <!--class="col-xs-12 col-sm-9 col-md-9 col-lg-9"></sa-big-breadcrumbs>-->

    <!--&lt;!&ndash;<div class="col-xs-12 col-sm-3 col-md-3 col-lg-3">&ndash;&gt;-->
    <!--&lt;!&ndash;&lt;!&ndash; Button trigger modal &ndash;&gt;&ndash;&gt;-->
    <!--&lt;!&ndash;<a (click)="mdModal.show()" class="btn btn-success btn-lg pull-right header-btn hidden-mobile"><i&ndash;&gt;-->
    <!--&lt;!&ndash;class="fa fa-circle-arrow-up fa-lg"></i> Launch form modal</a>&ndash;&gt;-->
    <!--&lt;!&ndash;</div>&ndash;&gt;-->
    <!--</div>-->


    <!-- widget grid -->

    <sa-widgets-grid>
        <ngx-loading [show]="loading"></ngx-loading>


        <!-- START ROW -->

        <div class="row">

            <!-- NEW COL START -->
            <article class="col-sm-12 col-md-12 col-lg-12">

                <!-- Widget ID (each widget will need unique ID)-->
                <div sa-widget [editbutton]="false" [custombutton]="false">

                    <header>
                        <span class="widget-icon"> <i class="fa fa-edit"></i> </span>

                        <h2>${Utils.spacedCapital(entity.name)} Form</h2>

                    </header>
                    <div>
                        <!-- widget content -->
                        <div class="widget-body no-padding">

                            <form [formGroup]="myForm" novalidate (ngSubmit)="onSubmit(myForm)" id="checkout-form"
                                  class="smart-form">

                                <#include "/angular/layout/form-template.ftl">

                                <footer>
                                    <button [permission]="permission.UPDATE_${Utils.upperUderscore(entity.name)}" type="submit" class="btn btn-primary">
                                        Submit
                                    </button>
                                </footer>
                            </form>

                        </div>
                        <!-- end widget content -->

                    </div>
                    <!-- end widget div -->

                </div>
                <!-- end widget -->

                <!-- Widget ID (each widget will need unique ID)-->

                <!-- end widget -->

            </article>
            <!-- END COL -->

        </div>

        <!-- END ROW -->

    </sa-widgets-grid>

    <!-- end widget grid -->


</div>