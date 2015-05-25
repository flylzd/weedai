package com.weedai.ptp.ui.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.ArticleDetail;
import com.weedai.ptp.model.ArticleList;
import com.weedai.ptp.model.ArticleRelated;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.Comment;
import com.weedai.ptp.model.CommentList;
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.ListViewUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.ListScrollView;
import com.weedai.ptp.view.SimpleValidateCodeView;
import com.weedai.ptp.volley.ResponseListener;

import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ArticleDetailActivity extends BaseActivity {

    private final static String TAG = "ArticleDetailActivity";

    private WebView webView;

    private ListView listView;
    private QuickAdapter<ArticleList> adapter;
    private List<ArticleList> newsList = new ArrayList<ArticleList>();

    private ListView listViewComment;
    private QuickAdapter<CommentList> adapterComment;
    private List<CommentList> commentList = new ArrayList<CommentList>();
    private List<CommentList> commentListMore = new ArrayList<CommentList>();
    private int page = 0;

//    private ListScrollView listScrollView;

    private View footerView;
    private TextView tvComments;

    private View layoutRelated;

    private LinearLayout layoutComments;

    private EditText etSendComment;
    //    private Button btnSend;
    private String sendComment;
    private View layoutSendComment;

    private ProgressDialog progressDialog;

    private String aid;

    private String siteId;

    private AlertDialog alertDialog;

    private SimpleValidateCodeView simpleValidateCodeView;
    private String valicode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        if (getIntent().hasExtra("aid")) {
            aid = getIntent().getStringExtra("aid");
        }

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_information;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        webView = (WebView) findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        adapter = new QuickAdapter<ArticleList>(ArticleDetailActivity.this, R.layout.listitem_article) {
            @Override
            protected void convert(BaseAdapterHelper helper, ArticleList item) {

                helper.setText(R.id.tvArticleTitle, DataUtil.urlDecode(item.name));
                helper.setText(R.id.tvArticleSubTitle, DataUtil.urlDecode(item.summary));
                helper.setText(R.id.tvArticleDate, item.publish);
                helper.setText(R.id.tvComments, DataUtil.urlDecode(item.comment));

                ImageView imageView = helper.getView(R.id.imgArticle);
                String url = item.litpic;
                if (!TextUtils.isEmpty(url)) {
                    url = Config.DEFAULT_IMG_URL + url;
                    ImageLoader.getInstance().displayImage(url, imageView);
                } else {
                    url = Config.DEFAULT_IMG_DOWNLOAD;
                    ImageLoader.getInstance().displayImage(url, imageView);
                }
            }
        };
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UIHelper.showArticleDetail(ArticleDetailActivity.this, newsList.get(position).id);
            }
        });

        layoutRelated = findViewById(R.id.layoutRelated);

        adapterComment = new QuickAdapter<CommentList>(ArticleDetailActivity.this, R.layout.listitem_comment) {
            @Override
            protected void convert(BaseAdapterHelper helper, CommentList item) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String addtime = sdf.format(Long.parseLong(item.addtime + "000"));

                helper.setText(R.id.tvTime, DataUtil.urlDecode(addtime));
                helper.setText(R.id.tvUsername, DataUtil.urlDecode(item.username));
                helper.setText(R.id.tvComments, DataUtil.urlDecode(item.comment));
            }
        };
        listViewComment = (ListView) findViewById(R.id.listViewComment);
        listViewComment.setAdapter(adapterComment);

//        listScrollView = (ListScrollView) findViewById(R.id.listScrollView);
//        listScrollView.setListView(listViewComment);

        layoutComments = (LinearLayout) findViewById(R.id.layoutComments);


        tvComments = (TextView) findViewById(R.id.tvComments);  //更多评论
//         footerView = getLayoutInflater().inflate(R.layout.view_article_detail_listview_bottom,null);
//        tvComments = (TextView) footerView.findViewById(R.id.tvComments);
        tvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (commentListMore == null || commentListMore.size() == 0) {
                    return;
                }
                int sizeMore = commentListMore.size();
                int size = commentList.size();
                if (size == sizeMore) {
                    return;
                }
                System.out.println("sizeMore == " + sizeMore);
                System.out.println("size == " + size);

                int count = (size + 4) > sizeMore ? (sizeMore - size) : 4;
                System.out.println("count == " + count);
                for (int i = size; i < size + count; i++) {
                    commentList.add(commentListMore.get(i));
                    addCommentLayoutItem(commentListMore.get(i));
                }

                size = commentList.size();
                if (size < sizeMore) {
                    tvComments.setText("查看更多评论");
                } else {
                    tvComments.setText("没有更多评论");
                }
//                adapterComment.replaceAll(commentList);
//                ListViewUtil.setListViewHeightBasedOnChildren(listViewComment);


            }
        });
//        listViewComment.addFooterView(footerView);
////        listView.addFooterView(view);
        ListViewUtil.setListViewHeightBasedOnChildren(listViewComment);

        etSendComment = (EditText) findViewById(R.id.etSendComment);
        layoutSendComment = findViewById(R.id.layoutSendComment);
        layoutSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Config.isLogin) {
                    Toast.makeText(ArticleDetailActivity.this, "请先登录，在发表评论...", Toast.LENGTH_SHORT).show();
                    UIHelper.showLogin(ArticleDetailActivity.this);
                    return;
                }

                sendComment = etSendComment.getText().toString();
                if (TextUtils.isEmpty(sendComment)) {
                    Toast.makeText(ArticleDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                View view = getLayoutInflater().inflate(R.layout.dialog_simple_validate_code_view, null);
                final EditText etValicode = (EditText) view.findViewById(R.id.etValicode);
                simpleValidateCodeView = (SimpleValidateCodeView) view.findViewById(R.id.viewValicode);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(params);
                simpleValidateCodeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getImgcode();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(ArticleDetailActivity.this);
                builder.setTitle("请输入验证码");
                builder.setView(view);
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create();
                alertDialog = builder.show();
                alertDialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code = etValicode.getText().toString();
                        if (TextUtils.isEmpty(code)) {
                            Toast.makeText(ArticleDetailActivity.this, getString(R.string.login_valicode_empty), Toast.LENGTH_SHORT).show();
                        } else {
                            if (!valicode.equalsIgnoreCase(code)) {
                                Toast.makeText(ArticleDetailActivity.this, getString(R.string.login_valicode_not_match), Toast.LENGTH_SHORT).show();
                                getImgcode();
                            } else {
                                System.out.println("sendComment " + sendComment);
                                String comment = URLEncoder.encode(sendComment);
                                System.out.println("comment " + comment);
                                addComment(aid, comment, valicode);
                            }
                        }
                    }
                });
                getImgcode();
            }
        });
        progressDialog = ProgressDialog.show(ArticleDetailActivity.this, null, getString(R.string.message_waiting));
    }

    private void loadData() {
        getArticleDetail();
//        getRelatedArticleList();
    }

    private void getArticleDetail() {

        ApiClient.getArticleDetail(TAG, aid, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                ArticleDetail result = (ArticleDetail) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleDetailActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String htmlString = DataUtil.urlDecode(result.data.content);
                System.out.println("name " + DataUtil.urlDecode(result.data.name));
                System.out.println("content " + DataUtil.urlDecode(result.data.content));
                // 载入这个html页面
//                webView.loadData(htmlString, "text/html", "utf-8");
                webView.loadData(getHtmlData(htmlString), "text/html; charset=utf-8", "utf-8");
//                webView.loadData(htmlString, "text/html; charset=utf-8", "utf-8");
//                webView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);

                siteId = result.data.site_id;

                getCommentList();
                getRelatedArticleList();
            }
        });
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    private void getRelatedArticleList() {
        ApiClient.getRelatedArticleList(TAG, siteId, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                ArticleRelated result = (ArticleRelated) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleDetailActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                List<ArticleList> articleList = result.data;
                newsList = articleList;
                adapter.replaceAll(newsList);

                ListViewUtil.setListViewHeightBasedOnChildren(listView);
            }
        });
    }

    private void getCommentList() {

        ApiClient.getCommentList(TAG, aid, page, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                Comment result = (Comment) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleDetailActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<CommentList> list = result.data.list;
//                commentList = list;
                commentListMore = list;

                layoutComments.removeAllViews();
                commentList.clear();
                int size = list.size();
                if (list == null || size == 0) {
                    tvComments.setText("没有评论");
                } else if (size > 4) {
                    tvComments.setText("查看更多评论");
                    for (int i = 0; i < 4; i++) {
                        commentList.add(commentListMore.get(i));
                    }
                } else {
                    tvComments.setText("没有更多评论");
                    commentList.addAll(list);
                }
//                listViewComment.addFooterView(footerView);
//                adapterComment.replaceAll(commentList);
//                ListViewUtil.setListViewHeightBasedOnChildren(listViewComment);
//                listViewComment.addFooterView(footerView);

                addCommentLayoutList(commentList);
            }
        });
    }

    private void addCommentLayoutList(List<CommentList> commentList) {

        for (CommentList item : commentList) {
            View view = getLayoutInflater().inflate(R.layout.listitem_comment, null);
            TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
            TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
            TextView tvComments = (TextView) view.findViewById(R.id.tvComments);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String addtime = sdf.format(Long.parseLong(item.addtime + "000"));

            tvTime.setText(DataUtil.urlDecode(addtime));
            tvUsername.setText(DataUtil.urlDecode(item.username));
            tvComments.setText(DataUtil.urlDecode(item.comment));

            layoutComments.addView(view);
        }
    }

    private void addCommentLayoutItem(CommentList item) {
        View view = getLayoutInflater().inflate(R.layout.listitem_comment, null);
        TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
        TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        TextView tvComments = (TextView) view.findViewById(R.id.tvComments);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String addtime = sdf.format(Long.parseLong(item.addtime + "000"));
        tvTime.setText(DataUtil.urlDecode(addtime));
        tvUsername.setText(DataUtil.urlDecode(item.username));
        tvComments.setText(DataUtil.urlDecode(item.comment));

        layoutComments.addView(view);
    }


    private void addComment(String id, String comment, String valicode) {
        ApiClient.addComment(TAG, id, comment, valicode, new RefreshResponseListener() {
            @Override
            public void onStarted() {
                super.onStarted();
                progressDialog.setMessage("正在提交数据...");
            }

            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                BaseModel result = (BaseModel) response;
//                if (result.code != Constant.CodeResult.SUCCESS) {
//                    Toast.makeText(ArticleDetailActivity.this, result.message, Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (result.message.equals("addcomment_success")) {
                    alertDialog.dismiss();
                    etSendComment.getText().clear();
//                    getArticleDetail();
                    getCommentList();
                } else {
                    Toast.makeText(ArticleDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getImgcode() {

        ApiClient.getImgcode(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
            }

            @Override
            public void onResponse(Object response) {

                Valicode result = (Valicode) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleDetailActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                valicode = result.data.code;
                System.out.println("valicode : " + valicode);
                String code = result.data.code;
                int length = code.length();
                String[] codes = new String[length];
                for (int i = 0; i < length; i++) {
                    codes[i] = String.valueOf(code.charAt(i));
                }
                simpleValidateCodeView.getValidataAndSetImage(codes);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }


    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            if (!progressDialog.isShowing()) {
                progressDialog.setMessage(getString(R.string.message_waiting));
                progressDialog.show();
            }
        }

        @Override
        public void onResponse(Object response) {
            progressDialog.dismiss();

        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            progressDialog.dismiss();
        }
    }


}
